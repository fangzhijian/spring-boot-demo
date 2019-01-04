package com.outdoor.club.model.specialRailway;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 2018/11/6 14:20
 * 走路呼呼带风
 * 专线设置
 */
@Data
@Accessors(chain = true)
public class SpecialSetting implements Serializable{

    private Double percentClub;      //梯俱乐部利润百分比
    private Double percentSupplier;  //供应商利润百分比
    private Double percentXZ;        //行装利润百分比
    private Double percentSpecial;   //专线利润百分比
    private Double percentCharge;    //手续费百分比
    private Long   specialPhone;     //专线联系方式

}
