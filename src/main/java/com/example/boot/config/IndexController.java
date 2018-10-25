package com.example.boot.config;

import com.example.boot.mapper.UserMapper;
import com.example.boot.model.Body;
import com.example.boot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2018/10/10 14:21
 * 走路呼呼带风
 */
@Controller
@Slf4j
public class IndexController {

    @Autowired
    public Body body;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/test")
    @ResponseBody
    public Object test(){
        log.info(body.getName());
        User user = userMapper.getById(1);
        user.setName(null);
        return user;
    }
}
