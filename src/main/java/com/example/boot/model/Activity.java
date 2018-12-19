package com.example.boot.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 2018/12/19 10:22
 * 走路呼呼带风
 */
@Data
@Alias("activity")
public class Activity {

    private Integer agencyId;
    private Integer viewCount;
}
