package com.example.boot.constans;


/**
 * 走路呼呼带风
 * redis key
 */
public interface RedisConstants {

    String CLUB_ADMIN_LOGIN = "club:admin:login:%s";          // 后台用户登入
    String SMS_USER = "sms:user:%s";                          //短信用户前缀
    String SMS_PHONE = "sms:phone:%s";                        //短信手机前缀
    String CLUB_ORDER_REFUND_PREFIX = "clubRefund:";          //售后订单
    String CLUB_APP_MANAGE = "club:app:manage";               //安卓app信息
    String SPECIAL_RAILWAY= "special:railway:setting";        //专线设置
    String ARTICLE_EDIT_PREFIX= "article:edit:";              //文章编辑
    String ACTIVITY_EDIT_PREFIX= "activity:edit:";            //活动编辑
    String MUSIC_RECORD_PREFIX ="music:club:%s";              //俱乐部的音乐使用记录
    String KU_GOU_LIBRARY_PREFIX ="kuGouLibrary:%s";          //俱乐部的音乐使用记录
    String RABBIT_RETYR_KEY = "rabbit:retry:%s";              //rabbit发送失败key
    String RABBIT_OVER_KEY = "rabbit:over:%s";                //rabbit超出尝试发送失败次数

}
