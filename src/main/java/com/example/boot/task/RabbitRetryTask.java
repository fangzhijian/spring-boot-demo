package com.example.boot.task;

import com.example.boot.constans.RedisConstants;
import com.example.boot.listener.rabbit.CorrelationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 2019/2/19 13:26
 * 走路呼呼带风
 */
@Component
@Slf4j
public class RabbitRetryTask {

    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String,Object> redisTemplate;

    public RabbitRetryTask(RabbitTemplate rabbitTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * rabbit mq定期重试发送失败的消息
     */
    @Scheduled(cron="0 * * * * ?")
    @Async
    public void resendMq(){
        log.info("开始定时扫描rabbit mq发送失败的消息队列");
        //获取发送失败的mq
        Set<String> keys = redisTemplate.keys(String.format(RedisConstants.RABBIT_RETYR_KEY,"*"));
        if (keys != null && !keys.isEmpty()){
            List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
                @Nullable
                @Override
                public Object doInRedis(@Nullable RedisConnection connection) throws DataAccessException {
                    if (connection != null) {
                        keys.forEach(key -> connection.hGetAll(key.getBytes()));
                    }
                    return null;
                }
            });

            //重新发送mq,并利用messageId设置重发次数加1,然后从redis重删除
            //当大于等于5次时,不再重发,从redis从删除,并记录该条超出尝试次数的消息
            for (Object obj:list){
                @SuppressWarnings("unchecked")
                Map<String,String> map = (Map<String, String>) obj;
                String exchange = map.get("exchange");
                String routingKey = map.get("routingKey");
                String[] messageIds = map.get("messageId").split("#");
                int retry = Integer.parseInt(messageIds[1]);
                String previousKey = String.format(RedisConstants.RABBIT_RETYR_KEY,messageIds[0]);
                if (retry>=5){
                    redisTemplate.delete(previousKey);
                    redisTemplate.opsForHash().putAll(String.format(RedisConstants.RABBIT_OVER_KEY,messageIds[0]),map);
                }else {
                    String messageId = String.format("%s#%s",messageIds[0],retry+1);
                    MessageProperties messageProperties = new MessageProperties();
                    messageProperties.setMessageId(messageId);
                    Message message = new Message(map.get("body").getBytes(),messageProperties );
                    CorrelationMessage correlationMessage = new CorrelationMessage(exchange,routingKey,message.getBody(),messageId);
                    rabbitTemplate.send(exchange,routingKey,message,correlationMessage);
                    redisTemplate.delete(previousKey);
                }

            }
        }
    }
}
