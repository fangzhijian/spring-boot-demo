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
 */
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    @AliasFor("redisKey")
    String value() default "";                      //redis的key值
    @AliasFor("value")
    String redisKey() default "";                   //redis的key值

    long expireTime() default 1L;                   //过期时间

    TimeUnit timeUnit() default TimeUnit.SECONDS;   //过期时间单位
}
