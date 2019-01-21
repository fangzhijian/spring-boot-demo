package com.example.boot.service.impl;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 2019/1/11 17:56
 * 走路呼呼带风
 */
@Service("userService1")
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    @Cacheable(cacheNames = "service",key = "#id")
    public User getUser(Integer id) {
        log.info("userService1:{}",id);
        User user = new User();
        user.setName("小明");
        user.setId(id);
        user.setAge(2);
        user.setCreateTime(LocalDateTime.now());
        return user;
    }
}
