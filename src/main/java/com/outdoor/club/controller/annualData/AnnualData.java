package com.outdoor.club.controller.annualData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 2019/1/25 17:55
 * 走路呼呼带风
 */
@Data
public class AnnualData implements Serializable{

    private Integer userId;         //用户id
    private String portrait;        //头像
    private String nickName;        //昵称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;        //用户创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date upgradeTime;        //升级开店时间
    private Integer signInNum;       //签到次数
    private Integer signIntegral;    //签到积分
    private Double shopPercent;      //超过店主百分比
    private Integer fansNum;         //粉丝数
    private Integer orderNum;        //订单数
    private Double saleroom;         //销售额
    private Integer annualIntegral;  //年度积分
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createClubTime;     //创建俱乐部时间
    private Integer articleNum;      //文章数
    private Integer articleView;     //文章浏览量
    private Integer activityNum;     //活动数
    private Integer activityView;    //活动浏览量
    private Integer activityApplyNum;//活动报名数
    private Integer inviteShop;      //邀请开店数
    private Integer shopIntegral;    //店主赚取积分
    private String topSaleDay;       //销售额最高的一天
    private Double topSaleroom;      //最高销售额
    private Integer topOrderNum;     //最高订单数

}
