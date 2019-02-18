package com.example.boot.listener.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 2019/2/15 16:03
 * 走路呼呼带风
 */
@Component
@Slf4j
public class RabbitCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private final ObjectMapper objectMapper;

    public RabbitCallback(@Qualifier("commonObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        if (!ack){
            String exchange = null;
            String routingKey = null;
            String body = null;
            if (correlationData instanceof CorrelationMessage){
                CorrelationMessage message = (CorrelationMessage) correlationData;
                exchange = message.getExchange();
                routingKey = message.getRoutingKey();
                body = new String(message.getBody());
                log.error(message.getMessageId());
                //异步记录到数据库,并用定时任务去扫描重发,并设置重发次数
                try {
                    //使用相同的序列化objectMapper得到类,并重新发送
                    //这里只是教如何反序列
                    System.out.println(objectMapper.readValue(body,Object.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error("消息发送到交换器失败,exchange={},routingKey={},body={},由于{}",exchange,routingKey,body,cause);
        }
    }

    @Override
    public void returnedMessage(@Nullable Message message, int replyCode,@Nullable String replyText,
                                @Nullable String exchange,@Nullable String routingKey) {
        if (message!= null){
            String body = new String(message.getBody());
            log.error(message.getMessageProperties().getMessageId()+"");
            log.error("消息发送到队列发送失败,exchange={},routingKey={},body={},由于{}",exchange,routingKey,body,replyText);
            //异步记录到数据库,并用定时任务去扫描重发,并设置重发次数
        }
    }
}
