package com.example.boot.util.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 * 走路呼呼带风
 */
public class NameThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String name;

    public NameThreadFactory(String name){
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,this.name+"第"+threadNumber.getAndIncrement()+"线程");
    }
}
