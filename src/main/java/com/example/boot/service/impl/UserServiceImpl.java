package com.example.boot.service.impl;

import com.example.boot.model.User;
import com.example.boot.service.UserService;

import java.time.LocalDateTime;

/**
 * 2019/1/11 17:56
 * 走路呼呼带风
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Integer id) {
        User user = new User();
        user.setName("小明");
        user.setId(1);
        user.setAge(2);
        user.setCreateTime(LocalDateTime.now());
        return user;
    }
}
