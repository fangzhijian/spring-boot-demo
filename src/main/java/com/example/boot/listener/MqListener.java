package com.example.boot.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 2019/1/23 16:45
 * 走路呼呼带风
 * RabbitListener
 */
@Component
@Slf4j
public class MqListener {

    /**
     * 当在类上面与@Component同位置标明@RabbitListener时,
     * RabbitHandler可以匹配接收不同的类型
     */
    @RabbitHandler
    public void demo(String name, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("rabbitmq已接收到消息:来者{}",name);
        if ("朱大明".equals(name)){
            //将消息重新放回准备队列,准备队列马上可以被消费,有可能造成无限消费循环
            channel.basicNack(tag,false,true);
        }else if ("朱小明".equals(name)){
            //从队列中丢弃该消息
            channel.basicReject(tag,false);
        }else {
            //确认消息
            channel.basicAck(tag,false);
        }
        //当没有任何确认操作时,消息将进入未确认队列
        //当java服务器链接断开停止时,消息会从未确认跑到准备队列中
        //这时消费者又可以消费该消息
    }

    /**
     *  用于接收用户服务更新会员身份同步俱乐部用户身份的队列
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue("club-memberType"),
            exchange = @Exchange(value = "club-exchange",type = "topic"),key = "club.memberType")})
    public void test(String json, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG)long tag) throws IOException {
        log.info("年轻人好生心浮气躁1:{}",json);
        channel.basicAck(tag,false);
    }


}
