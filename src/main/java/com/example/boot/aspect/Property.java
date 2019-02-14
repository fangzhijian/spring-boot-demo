package com.example.boot.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 2019/2/13 16:38
 * 走路呼呼带风
 * 同spring的@Value相比
 * 缺点: 1、只能作用在成员变量
 *       2、只支持基础数据类型String byte short int long double float boolean,
 *       3、限定了使用${}取值表达式
 * 优点: 1、省去 ${}取值表达式
 *       2、如果在公共jar中使用@Value,就必须在所有项目中都有配置,不然无法启动
 *       3、ctrl点击时会出现蓝色下边，可以快速定位
 * SpringUtil 可以支持更多的取值方式,支持设置静态变量和常量,任何地方
 * @see com.example.boot.util.SpringUtil
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    String value();

    /**
     *  是否在启动时检查value值在环境中是否存在
     *  默认true, 工具包中可以设置false,防止其他项目没有配置启动不了
     */
    boolean check() default true;
}
