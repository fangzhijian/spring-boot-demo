package com.example.boot.listener;

import lombok.extern.slf4j.Slf4j;
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

    @RabbitListener(queues = {"hello"})
    public void  hello(String str){
        log.info("提莫队长正在待命");
        log.info("收到报告:{}",str);
    }

}
