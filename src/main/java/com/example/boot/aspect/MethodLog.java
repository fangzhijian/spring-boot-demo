package com.example.boot.aspect;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 2018/9/18 9:49
 * 走路呼呼带风
 */
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

    LogType value() default LogType.AROUND; //日志切入点 BEFORE AFTER AROUND
}
