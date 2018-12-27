package com.example.boot.model.kuGou;

import lombok.Data;

import java.io.Serializable;

/**
 * 2018/12/27 13:51
 * 走路呼呼带风
 * 酷狗api返回json
 */
@Data
public class KuGouJson<T> implements Serializable{
    private Integer status;             //返回状态 0-失败 1-成功
    private Integer error_code;         //返回码 0-成功 其他都是失败
    private T data;                     //返回体
}
