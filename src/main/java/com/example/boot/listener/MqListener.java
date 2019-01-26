package com.example.boot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 2019/1/23 16:45
 * 走路呼呼带风
 */
@Component
@Slf4j
public class MqListener {


//    @RabbitListener(queues="club-order")
    public void process1(String user) {
        log.info("process1:"+user);
    }

}
