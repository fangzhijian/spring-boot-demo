package com.outdoor.club.redisBean;

import lombok.Data;

/**
 * 2018/8/11 11:30
 * 走路呼呼带风
 */
@Data
public class OrderExpireBean {

    private Long expireTime; //订单支付过期时间

    private String orderId;  //订单id

}
