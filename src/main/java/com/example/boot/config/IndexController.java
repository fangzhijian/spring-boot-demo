package com.example.boot.config;

import com.example.boot.mapper.UserMapper;
import com.example.boot.model.Body;
import com.example.boot.model.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Date;

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
    @Autowired
    private Gson gson;


    @GetMapping("/test")
    @ResponseBody
    public Object test(){
        log.info(body.getName());
        User user = userMapper.getById(1);
        user.setName(null);
        return user;
    }

    @GetMapping("abc")
    public String abc(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("id",3);
        redirectAttributes.addAttribute("name","猪大肠");
        redirectAttributes.addAttribute("createTime",new Date().toString());
        return "redirect:efg";
    }

    @GetMapping("efg")
    @ResponseBody
    public User efg(@Validated User user){
        return user;
    }

    @GetMapping("hij")
    @ResponseBody
    public User hij(String userJson){
        return gson.fromJson(userJson,User.class);
    }


}
