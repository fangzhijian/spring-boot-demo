package com.example.boot.listener.rabbit;

import com.example.boot.constans.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * 2019/2/15 16:03
 * 走路呼呼带风
 * 记录发送失败的消息,并利用定时任务尝试重发
 * @see  com.example.boot.task.RabbitRetryTask
 */
@Slf4j
public class RabbitCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private final RedisTemplate<String,Object> redisTemplate;

    public RabbitCallback(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        if (!ack && correlationData instanceof CorrelationMessage){
            CorrelationMessage message = (CorrelationMessage) correlationData;
            String exchange = message.getExchange();
            String routingKey = message.getRoutingKey();
            String body = new String(message.getBody());
            String messageId = message.getMessageId();
            //异步记录到数据库,并用定时任务去扫描重发,并设置重发次数
            saveFailedMqToRedis(messageId,exchange,routingKey,body,cause);
            log.error("消息发送到交换器失败,messageId={},exchange={},routingKey={},body={},由于{}",messageId,exchange,routingKey,body,cause);
        }
    }

    @Override
    public void returnedMessage(@Nullable Message message, int replyCode,@Nullable String replyText,
                                @Nullable String exchange,@Nullable String routingKey) {
        if (message!= null){
            String body = new String(message.getBody());
            String messageId = message.getMessageProperties().getMessageId();
            log.error("交换器发送到队列失败,messageId={},exchange={},routingKey={},body={},由于{}",message,exchange,routingKey,body,replyText);
            //异步记录到数据库,并用定时任务去扫描重发,并设置重发次数
            saveFailedMqToRedis(messageId,exchange,routingKey,body,replyText);
        }
    }

    /**
     *
     * @param messageId     消息id,由redis自增id加#n组成,#n表示尝试重新发送次数
     * @param exchange      交换器
     * @param routingKey    路由键
     * @param body          消息体
     * @param cause         失败原因
     */
    private void saveFailedMqToRedis(String messageId,String exchange,String routingKey,String body,String cause){
        //当 messageId = 0时获取redis自增id
        if ("0".equals(messageId)){
            Long rabbitId = redisTemplate.opsForValue().increment("rabbitId");
            if (rabbitId != null){
                //#0代表尝试重新发送次数为0
                messageId = rabbitId.toString()+"#0";
            }else {
                return;
            }
        }
        Map<String,String> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("exchange",exchange);
        map.put("routingKey",routingKey);
        map.put("body",body);
        map.put("cause",cause.length()>=30?cause.substring(0,30):cause);
        String redisKey = String.format(RedisConstants.RABBIT_RETYR_KEY,messageId.substring(0,messageId.indexOf("#")));
        redisTemplate.opsForHash().putAll(redisKey,map);
    }
}
