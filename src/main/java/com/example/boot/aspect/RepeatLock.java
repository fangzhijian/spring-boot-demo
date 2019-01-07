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
 * redis分布式锁,一般用于防止重复提交,未拿到锁时直接返回失败,常用示例如下
 * //@RedisLock("#id")
 * //@RedisLock(value = "123",deleteFinish = true,expireTime = 5000)
 * //@RedisLock(value = "#u.name",prefixKey = "order:",expireTime = 3,timeUnit = TimeUnit.SECONDS)
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatLock {

    /**
     *  redis的key前缀,默认值lock:repeat:加方法名,
     *  如方法getUser,key前缀就是lock:repeat:getUser:
     *  当不使用默认时,如prefixKey = "order:"就是lock:repeat:order:,注意以冒号结尾
     */
    String prefixKey() default "";

    /**
     *  redis的key后缀
     *  1、固定值,不加#号首写,如suffixKey = "100"就是固定的100
     *  2、#加入参参数，如suffixKey = "#id"，#必须为首写
     *  3、#加入参实体类加.实体类变量，如suffixKey = "#user.id"
     */
    @AliasFor("value")
    String suffixKey() default "";

    @AliasFor("suffixKey")
    String value() default "";                           //redis的key后缀

    long expireTime() default 1200L;                     //过期时间,默认1.2秒,在结束不删除时即单位时间内只能成功请求一次

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;   //过期时间单位,默认豪秒

    boolean  deleteFinish() default false;               //方法执行完时是否需要删除锁,默认不删除
}
