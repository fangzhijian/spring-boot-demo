package com.example.boot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 2018/10/10 16:40
 * 走路呼呼带风
 */
public class Test {
    public static void main(String[] args) throws Exception{

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(111);
            }
        },3, TimeUnit.SECONDS);
        System.out.println(222);
    }
}
