package com.example.boot.aspect;

import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 2019/1/7 13:05
 * 走路呼呼带风
 * redis分布式锁,一般用于竞争有限资源,未拿到锁时一直尝试,直到超时
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceLock {
    /**
     *  redis的key前缀,默认值lock:resource:加方法名,
     *  如方法getUser,key前缀就是lock:resource:getUser:
     *  当不使用默认时,如prefixKey = "order:"就是lock:resource:order:,注意以冒号结尾
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

    long expireTime() default 1500L;                     //过期时间,默认1.5秒

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;   //过期时间单位,默认豪秒

    int interVal() default 100;                          //未拿到锁时,重新尝试间隔,默认100毫秒,单位不受timeUnit影响

    int timeOut() default 4500;                          //未拿到锁时,重新尝试最大时间,默认4500毫秒加间隔时间,单位不受timeUnit影响

}
