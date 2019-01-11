package com.example.boot.config;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 2019/1/11 17:25
 * 走路呼呼带风
 */
@Slf4j
public class  JdkProxy implements InvocationHandler {

    //如果使用同一个JdkProxy会存在object的线性安全问题
    //可以将object写为一个map,每次注入的时候bean名称就是key,value就是静态示例
    //spring使用map注册的单列模式,可以完美的使用动态代理
    private Object object;

    public static JdkProxy getInstance(){
        return new JdkProxy();
    }

    public Object getObject(Object object) {
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("代理前请你吃饭");
        Object invoke = method.invoke(object, args);
        log.info("代理后请你吃屁");
        return invoke;
    }
}
