package com.example.boot.model;

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

    @NotNull
    @Max(value = 3,message = "id长度不能超过3")
    private Integer id;
//    @Pattern(regexp = "\\d*@.*\\.com")
    @NotNull
    private String name;
//    @NotNull(message = "不能为空")
    private Date createTime;
}
