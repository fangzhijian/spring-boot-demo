package com.example.boot.controller;

import com.example.boot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2019/1/21 9:19
 * 走路呼呼带风
 */
@RestController
@RequestMapping("cache")
@Slf4j
public class CacheController {


    @PostMapping
    @Cacheable(cacheNames = "cache",key = "#id")
    public User user(Integer id){
        log.info("insert{}",id);
        return new User().setId(id).setName("猪大肠").setAge(18);
    }


    @DeleteMapping
    @CacheEvict(cacheNames = "cache",key = "#id")
    public void delete(Integer id){
        log.info("删除了缓存cache:{}",id);
    }

    @PutMapping
    @CachePut(cacheNames = "cache",key = "#id")
    public User put(Integer id){
        log.info("put{}",id);
        return new User().setId(id).setName("猪小肠").setAge(6);
    }

}
