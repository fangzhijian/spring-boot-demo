package com.example.boot.controller;

import com.example.boot.model.User;
import com.example.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 2019/1/21 9:19
 * 走路呼呼带风
 */
@RestController
@RequestMapping("cache")
@Slf4j
public class CacheController {

    private final UserService userService;

    public CacheController(@Qualifier("userService1") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Cacheable(cacheNames = "demo",key = "#type+':'+#id")
    public User user(Integer id,Integer type){
        log.info("insert:{}",id);
        return new User().setId(id).setName("猪大肠").setAge(18);
    }


    @DeleteMapping
    @CacheEvict(cacheNames = "demo",key = "#result.id")
    public User delete(Integer id){
        log.info("删除了缓存cache:{}",id);
        return new User().setId(id);
    }

    @PutMapping
    @CachePut(cacheNames = "demo",key = "#id")
    public User put(Integer id){
        log.info("put:{}",id);
        return new User().setId(id).setName("猪小肠").setAge(6);
    }

    @GetMapping("user")
    @CachePut(cacheNames = "user",key = "0")
    public String user(){
        return "我叫小明";
    }

    @DeleteMapping("user")
    @CacheEvict(cacheNames = "user",key = "1")
    public void userEvict(){
        log.info("删除了缓存user::1");
    }

    @GetMapping("service")
    public User service(Integer id, HttpServletResponse response){
        log.info("service:{}",id);
        Cookie cookieNme = null;
        response.addCookie(null);
        return userService.getUser(id);
    }
}
