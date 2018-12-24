package com.example.boot.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/12/24 19:31
 * 走路呼呼带风
 */
@Data
public class ExcelData implements Serializable{

    private List<String> titles;    //表头
    private List<List<Object>> rows; //数据
    private String name; //页签名称

}