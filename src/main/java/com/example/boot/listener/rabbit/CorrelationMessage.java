package com.example.boot.listener.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.io.Serializable;

/**
 * 2019/2/15 14:44
 * 走路呼呼带风
 */
@Data
@AllArgsConstructor
public class CorrelationMessage extends CorrelationData implements Serializable {

    private String exchange;   //交换器
    private String routingKey; //路由键
    private byte[] body;       //消息实体
    private String messageId;  //消息id,可用于更新重新发送次数

}
