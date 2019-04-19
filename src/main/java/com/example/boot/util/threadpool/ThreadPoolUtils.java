package com.example.boot.util.threadpool;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 * 走路呼呼带风
 *
 */
public class ThreadPoolUtils {

    /**
     * 服务器cpu物理核心数的2倍
     */
    private static int threadCount = Runtime.getRuntime().availableProcessors();
    /**
     * 常用于并行处理流程,用于加快流程速度
     * 无界单边队列线程,超出核心线程数时就排队等待
     * 本线程-->核心数:threadCount,最大线程数:threadCount,线程名称:线程池fixedExecutor
     *          非核心线程超时时间:0微秒,超过这个时间就会被回收
     */
    public static final ThreadPoolExecutor fixedExecutor = new ThreadPoolExecutor(threadCount,threadCount,0L,TimeUnit.MICROSECONDS,
            new LinkedBlockingQueue<>(),new NameThreadFactory("线程池fixedExecutor"));
    /**
     *
     * 常用于异步处理流程,同MQ异步处理流程
     * 无队列线程,超出核心线程数就创建新的线程,
     * 当线程数大于最大线程数时,即调用饱和策略
     * 本线程-->核心数:0,最大线程数:200,线程名称:线程池cachedExecutor
     *          非核心线程超时时间:0微秒,超过这个时间就会被回收
     *          饱和策略:使用自定义策略调用线程池fixedExecutor继续跑超出最大线程数的线程
     *                   (系统自带策略要么抛异常要么放弃新老线程均不可靠)
     */
    public static final ThreadPoolExecutor cachedExecutor = new ThreadPoolExecutor(0,200,0L, TimeUnit.MICROSECONDS,
            new SynchronousQueue<>(),new NameThreadFactory("线程池cachedExecutor"),new ThreadRejectStrategy());

    private ThreadPoolUtils(){
    }



}
