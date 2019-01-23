package com.example.boot.service.impl;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 2019/1/11 17:56
 * 走路呼呼带风
 */
@Service("userService1")
@Slf4j
public class UserServiceImpl implements UserService {

    private boolean flag = true;

    @Override
//    @Cacheable(cacheNames = "service",key = "#id")
    public User getUser(Integer id) {
        flag = !flag;
        Instant instant = Instant.now();
        long a=0;
        for (int i = 0; i <Integer.MAX_VALUE ; i++) {
            a = a+i;
        }
        System.out.println(a);
        System.out.println(Duration.between(instant,Instant.now()).toMillis());
        log.info("userService1:{}",id);
        User user = new User();
        user.setName("小明");
        user.setId(id);
        user.setAge(2);
        user.setCreateTime(LocalDateTime.now());
        if (flag){
            System.out.println(10/0);
        }
        return user;
    }
}
