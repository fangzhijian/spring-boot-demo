package com.example.boot.service.impl;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 2019/1/15 12:03
 * 走路呼呼带风
 */
@Service("userService2")
public class UserService2Impl implements UserService{
    @Override
    public User getUser(Integer id) {
        return new User().setName("小红");
    }
}
