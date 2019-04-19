package com.example.boot.util.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 * 走路呼呼带风
 */
@Slf4j
public class ThreadRejectStrategy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("线程已超出最大连接数,开始调用线程池fixedExecutor");
        ThreadPoolUtils.fixedExecutor.submit(r);
        }
}
