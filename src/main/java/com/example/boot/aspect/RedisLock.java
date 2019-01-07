package com.example.boot.aspect;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 2019/1/4 15:58
 * 走路呼呼带风
 * redis分布式锁注解,常用示例如下
 * //@RedisLock("#id")
 * //@RedisLock(value = "123",deleteFinish = false,expireTime = 5)
 * //@RedisLock(value = "#user.id",prefixKey = "order:",expireTime = 3500,timeUnit = TimeUnit.MILLISECONDS)
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     *  redis的key前缀,默认值lock:加方法名,
     *  如方法getUser,key前缀就是lock:getUser:
     *  当不使用默认时,如prefixKey = "order:"就是lock:order:
     */
    String prefixKey() default "lock:";

    /**
     *  redis的key后缀
     *  1、固定值,不加#号首写,如suffixKey = "100"就是固定的100
     *  2、#加入参参数，如suffixKey = "#id"，#必须为首写
     *  3、#加入参实体类加.实体类变量，如suffixKey = "#user.id"
     */
    @AliasFor("value")
    String suffixKey() default "";

    @AliasFor("suffixKey")
    String value() default "";                      //redis的key后缀

    long expireTime() default 1L;                   //过期时间,默认1秒

    TimeUnit timeUnit() default TimeUnit.SECONDS;   //过期时间单位,默认秒

    boolean  deleteFinish() default true;            //方法执行完时是否需要删除锁,默认不删除
}
