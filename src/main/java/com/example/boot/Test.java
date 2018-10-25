package com.example.boot;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 2018/10/10 16:40
 * 走路呼呼带风
 */
public class Test {
    public static void main(String[] args) throws Exception{

        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.34:3306/test?characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false","root","root");
        System.out.println(connection.isReadOnly());
        System.out.println("我爱你");
    }
}
