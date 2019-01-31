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
