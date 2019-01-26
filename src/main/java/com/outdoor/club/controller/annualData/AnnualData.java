package com.outdoor.club.controller.annualData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 2019/1/25 17:55
 * 走路呼呼带风
 */
@Data
public class AnnualData implements Serializable{

    private Integer userId;                                 //用户id
    private String portrait;                                //头像
    private String nickName = "亲爱的行装店主";             //昵称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;                                //用户创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date upgradeTime;                               //升级开店时间
    private Integer signInNum = 0;                          //签到次数
    private Integer signIntegral = 0;                       //签到积分
    private BigDecimal shopPercent;                         //超过店主百分比,默认给你23.66
    private Integer fansNum = 0;                            //粉丝数
    private Integer orderNum = 0;                           //订单数
    private BigDecimal saleroom = BigDecimal.ZERO;          //销售额
    private Integer annualIntegral = 0;                     //年度积分
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createClubTime;                            //创建俱乐部时间
    private Integer articleNum = 0;                         //文章数
    private Integer articleView = 0;                        //文章浏览量
    private Integer activityNum = 0;                        //活动数
    private Integer activityView = 0;                       //活动浏览量
    private Integer activityApplyNum = 0;                   //活动报名数
    private Integer inviteShop = 0;                         //邀请开店数
    private Integer shopIntegral = 0;                       //店主赚取积分
    private String topSaleDay = "2018-12-31";               //销售额最高的一天
    private BigDecimal topSaleroom  = BigDecimal.ZERO;      //最高销售额
    private Integer topOrderNum = 0;                        //最高订单数
    private Integer memberType;                             //会员类型 2-店主 3-代理商 4-高级代理商
    private BigDecimal saleroomPercent;                     //销售百分比,默认给你32.28
    private Integer label;                                  //标签 1-6 按照最后一页的标签顺序
}
