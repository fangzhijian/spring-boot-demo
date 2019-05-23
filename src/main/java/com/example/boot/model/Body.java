package com.example.boot.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 2018/10/10 15:23
 * 走路呼呼带风
 */
@Data
@ConfigurationProperties(prefix = "body")
@Component
@Accessors(chain = true)
public class Body implements Serializable{

    private Long id;
    private String name;
}
