package com.example.boot.model;

import com.example.boot.util.RegexpTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{

    @NotNull
    @Max(value = 3,message = "id长度不能超过3")
    private Integer id;
//    @Pattern(regexp = "\\d*@.*\\.com")
    @NotNull
    private String name;
//    @NotNull(message = "不能为空")
    @Pattern(regexp = RegexpTemplate.DATE_ALL,message = "格式不正确")
    private String createTime;
}
