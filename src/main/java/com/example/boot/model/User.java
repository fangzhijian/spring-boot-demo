package com.example.boot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 2018/10/10 16:26
 * 走路呼呼带风
 */
@Data
@Alias("user")
public class User implements Serializable{

    @NotNull(message = "id不能为空")
    @Max(value = 3,message = "id长度不能超过3")
    private Integer id;
    @Pattern(regexp = "\\d*@.*\\.com",message = "name格式不正确")
    @NotNull(message = "name不能为空")
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
