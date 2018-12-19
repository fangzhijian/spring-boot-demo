package com.example.boot.mapper;

import com.example.boot.model.Activity;
import com.example.boot.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 2018/10/10 16:25
 * 走路呼呼带风
 */
@Repository
public interface UserMapper {

    User getById(Integer id);

    List<Activity> getViewCountList();

    void updateViewCount(List<Activity> list);
}
