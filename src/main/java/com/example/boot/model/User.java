package com.example.boot.model;

import com.example.boot.util.RegexpTemplate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Max(value = 3,message = "id长度不能超过3")
    private Integer id;
//    @Pattern(regexp = "\\d*@.*\\.com")
    private String name;
//    @NotNull(message = "不能为空")
//    @Pattern(regexp = RegexpTemplate.DATE_ALL,message = "格式不正确")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime = LocalDateTime.now();
    private Integer age;//年龄
    private Date date = new Date();
}
