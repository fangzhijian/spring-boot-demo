package com.example.boot.service.impl;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import java.util.concurrent.Executors;

/**
 * 2019/1/15 12:03
 * 走路呼呼带风
// * BeanPostProcessor
 */
@Service("userService2")
public class UserService2Impl implements UserService,EmbeddedValueResolverAware{


    public static StringValueResolver stringValueResolver;

    @Override
    public User getUser(Integer id) {
        final String s = stringValueResolver.resolveStringValue("${baseUrl}");
        System.out.println(s);
        return new User().setName("小红");
    }

    @Override
    public void setEmbeddedValueResolver(@Nullable StringValueResolver resolver) {
        UserService2Impl.stringValueResolver = resolver;
    }
}
