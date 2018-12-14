package com.outdoor.club.model.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * 俱乐部配置管理
 */
@Data
public class ParamConfig implements Serializable{

    /**
     * 备注
     */
    private String describe;
    /**
     * key值
     */
    private Integer key;
    /**
     * value值1
     */
    private String value;
    /**
     * value值2
     */
    private String anotherValue;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 修改时间
     */
    private String update_time;

}
