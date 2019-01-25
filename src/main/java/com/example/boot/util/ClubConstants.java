package com.example.boot.util;



/**
 * 俱乐部常量类
 */
public class ClubConstants {

    /**
     * 后台参数配置key值
     */
    public static final int BACK_ADVERTORIAL = 100; //软文地址
    public static final int BACK_COMPRESS_PICTURE = 200; //素材图片压缩比例参数
    public static final int BACK_COMPRESS_ADVERT = 201; //广告图片压缩比例参数
    public static final int BACK_COMPRESS_ADVERT_MIN = 202; //广告图片小于value1 kb就不进行压缩
    public static final int BACK_MINI_APP_VERSION = 300; //小程序是否使用首页数据,用于审核,不同版本轮流使用300.400
    public static final int BACK_MINI_APP_NEW_VERSION = 400; //小程序是否使用首页数据,用于审核,不同版本轮流使用300.400
    public static final int BACK_MINI_APP_ACTIVITY_PUSH = 501; //小程序活动通知  value1是模板id  values2  page
    public static final int BACK_MINI_APP_ARTICLE_PUSH = 502; //小程序文章通知  value1是模板id  values2  page
    public static final int BACK_PUSH_USER_LIMIT = 600; //value1小程序中用户可被推送次数,value2俱乐部可推送次数
    public static final int BACK_CLUB_VISIT_LIMIT = 700; //value1代表7天访客同一个最多记录几次
    public static final int BACK_CLUB_ISSUE_TIP = 801; //value1代表发布活动提示
    public static final int BACK_CLUB_RECHARGE_TIP = 802; //value1代表充值提示
    public static final int BACK_CLUB_SETTLEMENT_TIP = 803; //value1代表结算提示
    public static final int BACK_CLUB_HX_TIP = 804; //value1代表环迅支付说明
    public static final int BACK_CLUB_COMPUTER_OPERA_TIP=805;//value1 电脑操作提示描述 value2电脑端网址
    public static final int BACK_CLUB_AGENCY_WARM_TIP=806;//value1 代理活动温馨提示
    public static final int BACK_CLUB_OFFLINE_WARM_TIP=807;//value1 专线活动下线温馨提示
    public static final int BACK_CLUB_REFUND_TIP=808;   //value1不支持退款提示,value2全额退款提示
    public static final int BACK_CLUB_ADVERT_CONFIG = 900; //value1 轮播图数量,value2轮播图播放顺序1-顺序展示,2-随机展示
    public static final int BACK_CLUB_PLATFORM_SHARE=1000;//value1 平台技术共享开头描述,value2 紧急人联系方式
    public static final int BACK_CLUB_UPLOAD_VIDEO_MAX=1100;//value1 上传视频最大容量,单位M
    public static final int BACK_CLUB_MINI_APP_SHARE=1200;//value1 小程序分享描述
    public static final int BACK_CLUB_CLEAN_PUSH_PERIOD=1300;//value1 小程序群发记录清理周期
    public static final int BACK_CLUB_SPECIAL_INTRODUCE=1400;//value1 	专线文字描述 value2 	专线视频地址
    public static final int BACK_CLUB_SPECIAL_OPEN=1500; //value1 	专线开关 0-开启 1-关闭
    public static final int BACK_CLUB_SPECIAL_PROTOCOL=1600;//vale1专线协议地址 value2专线协议名称
    public static final int BACK_CLUB_SPECIAL_STATISTICS_URL =1601;//vale1申请专线打开的url
    public static final int BACK_CLUB_SWITCH_MUSIC =1700;//vale1背景音乐开关 0-关 1-开



    /**
     * 无名用户
     */
    public static final String CLUB_NONE_NAME_USER = "无名用户";
    /**
     * 固定的id名称等
     */
    public static final Integer USER_ID_XZ = 712;                   //行装账户用户id
    public static final Integer USER_ID_CLOUD_ROAD = 1106;          //云途用户id
    public static final Integer USER_ID_CLOUD_ROAD_SUPPLIER = 1105; //云途供应商id
    public static final Integer SPECIAL_ID_CLUB_ROAD = 1;           //云途专线专线id
    public static final String SPECIAL_NAME_CLUB_ROAD = "云途专线"; //云途专线专线名称
    public static final Integer SUPPLIER_ID_XZ = 1;                 //行装专线供应商id
    public static final Integer SUPPLIER_ID_CLUB_ROAD = 2;          //云途专线供应商id

    /**
     * 小程序推送限制用户类型
     */
    public static final int PUSH_USER_TYPE_USER = 1;//小程序推送限制普通用户
    public static final int PUSH_USER_TYPE_CLUB = 2;//小程序推送限制俱乐部
    /**
     * 小程序推送状态
     */
    public static final int PUSH_STATUS_FAILED = 0; //小程序推状态失败
    public static final int PUSH_STATUS_SUCCESS = 1;//小程序推送状成功
    public static final int PUSH_STATUS_UNUSED = 2; //小程序推送状未被使用
    public static final int PUSH_STATUS_PUSHING= 3; //小程序推送状正在推送中
    public static final int PUSH_STATUS_LIMIIT= 4; //小程序推送状被推人次数受限
    /**
     * 活动营销类型
     */
    public static final int ACTIVITY_MARKETING_UNSET = 0; //未设置
    public static final int ACTIVITY_MARKETING_STAIR = 1; //阶梯价
    public static final int ACTIVITY_MARKETING_TIME = 2;  //限时特惠
    public static final int ACTIVITY_MARKETING_AGE = 3;  //年龄价
    /**
     * 营销子类型
     */
    public static final int ACTIVITY_MARKETING_ORIGIN =0 ; //原价
    public static final int ACTIVITY_MARKETING_BIRD =1 ;   //阶梯价-早鸟价
    public static final int ACTIVITY_MARKETING_SUPPER =2 ;  //阶梯价-特惠价
    public static final int ACTIVITY_MARKETING_TIME_CHILD =3 ;   //限时特惠价
    public static final int ACTIVITY_MARKETING_AGE_CHILD =4 ;   //限时特惠价


    /**
     * 俱乐部访客类型
     */
    public static final int CLUB_VISIT_USER_AUTH = 0;    //俱乐部访客类型授权用户
    public static final int CLUB_VISIT_USER_TOURIST = 1; //俱乐部访客类型游客
    /**
     * 俱乐部访客来源
     */
    public static final int CLUB_VISIT_SOURCE_ANDROID=1; //来源安卓
    public static final int CLUB_VISIT_SOURCE_IOS=2;     //来源IOS
    public static final int CLUB_VISIT_SOURCE_MINI_APP=3;//来源小程序
    public static final int CLUB_VISIT_SOURCE_WEB=4;     //来源web端
    public static final int CLUB_VISIT_SOURCE_WEB_MINI =5; //来源同时看过小程序和web
    /**
     * 访客访问来源
     */
    public static final int CLUB_READ_FROM_ARTICLE =0;    //文章
    public static final int CLUB_READ_FROM_ACTIVITY =1;   //活动
    public static final int CLUB_READ_FROM_CLUB =2;       //俱乐部首页
    /**
     * 群发是否被禁用
     */
    public static final int PUSH_AUTH_OK=0;         //群发正常
    public static final int PUSH_AUTH_FORBIDDEN=1;  //群发被禁用

    /**
     * 活动状态
     */
    public static final int AC_STATUS_PUBLIC = 1;   //公开的
    public static final int AC_STATUS_DRAFT = 2;    //草稿中
    public static final int AC_STATUS_SOLD_OUT = 3; //已下架
    public static final int AC_STATUS_DELETED = 4;  //已删除
    public static final int AC_STATUS_EXPIRE = 5;   //已过期
    public static final int AC_STATUS_MINI_APP = 6; //这个状态只是用来区别调用端是小程序
    public static final int AC_STATUS_OFF_LINE = 7; //已下线
    public static final int AC_STATUS_AUDIT = 8; //审核中

    /**
     * 活动专线类型
     */
    public static final int AC_SPECIAL_NORMAL = 0;          //非专线
    public static final int AC_SPECIAL_ORIGIN = 1;          //源专线
    public static final int AC_SPECIAL_AGENCT = 2;          //代理中的专线
    public static final int AC_SPECIAL_AGENCY_CANCEL = 3;   //已取消代理的专线


    /**
     * 订单状态
     */
    public static final int CLUB_ORDER_WAITPAY = 0;  //待付款
    public static final int CLUB_ORDER_PAYED = 1;    //已支付
    public static final int CLUB_ORDER_FINISH = 2;   //订单正常完成（不可售后）
    public static final int CLUB_ORDER_DELETE = 3;   //已删除
    public static final int CLUB_ORDER_CLOSE = 4;    //订单关闭（超过15分钟未支付）

    public static final int CLUB_ORDER_WAIT_DEAL_REFUND = 5;//待处理退款 用户申请售后还未处理的，已全部处理过的将不显示在这里
    public static final int CLUB_ORDER_APPLY_SUCCESS = 6;//报名成功 订单状态：已支付、已完成 售后状态：全部取消或者全被拒绝
    public static final int CLUB_ORDER_AFTERMARKET = 7;//售后 用户涉及到的申请售后的订单 包含部分退款的 免费活动部分退出
    public static final int CLUB_ORDER_AFTERMARKET_FINISH = 8;//售后 用户涉及到的申请售后的订单 包含已退款的 免费活动已退出
    public static final int CLUB_ORDER_FREE = 9;//免费活动报名成功
    /**
     * 订单支付状态
     */
    public static final int CLUB_PAY_STATUS_NONPAY = 0;    //未支付
    public static final int CLUB_PAY_STATUS_PAYED = 1;    //已支付

    /**
     * 订单支付方式
     */
    public static final int CLUB_ORDER_PAYMODEL_WX = 0;     //微信支付
    public static final int CLUB_ORDER_PAYMODEL_HX = 1;     //环迅支付


    /**
     * 订单列表请求数据
     */
    public static final Integer reqType1 = 1;       //全部订单
    public static final Integer reqType2 = 2;       //待付款订单
    public static final Integer reqType3 = 3;       //待处理退款订单
    public static final Integer reqType4 = 4;       //报名成功订单
    public static final Integer reqType5 = 5;       //已退出订单


    /**
     * 订单售后状态
     */
    public static final int CLUB_REFUND_INTIT_TRUE = 99;  //未申请售后初始状态
    public static final int CLUB_REFUND_INIT_FALSE = 98;  //订单完成不可申请
    public static final int CLUB_REFUND_INIT_FREE = 97;  //免费活动初始状态
    public static final int CLUB_REFUND_WAIT_DEAL_WITH = 0;  //待处理售后
    public static final int CLUB_REFUND_AGREE_REFUND_RIGHT_SEND_BACK = 1;  //同意退款（一同意真的就立马退了）、批量退款成功
    public static final int CLUB_REFUND_REFUSE_REFUND = 2;  //拒绝退款（拒绝一次就不可以再申请退款）
    public static final int CLUB_REFUND_CANCEL = 3;  //售后取消

    public static final int CLUB_REFUND_WAIT_PAYED = 4;//未支付
    public static final int CLUB_REFUND_APPLY_SUCCESS = 5;//报名成功
    public static final int CLUB_REFUND_APPLY_FINISH = 6;//报名完成

    public static final int CLUB_BATCH_REFUNDING = 7;//批量退款中
    public static final int CLUB_BATCH_REFUNDING_FAILED = 8;//批量退款失败
    public static final int CLUB_REFUND_FREE_OUT = 9;//免费活动退出
    public static final int CLUB_REFUND_BATCH_AGREE_REFUND_RIGHT_SEND_BACK = 10;  //批量退款成功



    /**
     * 售后流水角色
     */
    public static final Integer CLUB_REFUND_FLOW_BUSINESS = 0;//俱乐部经营者
    public static final Integer CLUB_REFUND_FLOW_CONSUMER = 1;//用户





    /**
     * 订单售后流水事项(动作)
     */
    public static final int CLUB_REFUND_FLOW_APPLY_C = 0;//c端申请售后动作
    public static final int CLUB_REFUND_FLOW_AGREE_B = 1;//b端同意退款动作
    public static final int CLUB_REFUND_FLOW_REFUSE_B = 2;//b端拒绝退款动作
    public static final int CLUB_REFUND_FLOW_CANCEL_C = 3;//c端取消申请售后动作
    public static final int CLUB_REFUND_FLOW_SUCCESS_HX = 4;//请求环迅受理退款成功
    public static final int CLUB_REFUND_FLOW_FAILED_HX = 5;//请求环迅受理退款失败
    public static final int CLUB_REFUND_FLOW_BATCH_START = 6;//俱乐部经营者发起批量退款
    public static final int CLUB_REFUND_FLOW_BATCH_SUCCESS = 7;//批量退款成功
    public static final int CLUB_REFUND_FLOW_BATCH_FAILED = 8;//批量退款失败

    /**
     * 售后流水标题
     */
    public static final String CLUB_REFUND_FLOW_TITLE_0 = "申请退款";//c端申请售后动作标题
    public static final String CLUB_REFUND_FLOW_TITLE_1 = "退款审核成功";//b端同意退款动作标题
    public static final String CLUB_REFUND_FLOW_TITLE_2 = "退款已拒绝";//b端拒绝退款动作标题
    public static final String CLUB_REFUND_FLOW_TITLE_3 = "退款已取消";//c端取消申请售后动作标题
    public static final String CLUB_REFUND_FLOW_TITLE_4 = "已受理退款成功";//请求环迅受理退款成功标题
    public static final String CLUB_REFUND_FLOW_TITLE_5 = "退款失败";//请求环迅受理退款失败标题
    public static final String CLUB_REFUND_FLOW_TITLE_6 = "俱乐部经营者发起退款";//俱乐部经营者主动发起批量退款标题

    /**
     * 批量退款说明
     */
    public static final String CLUB_BATCH_REFUND_EXPLAIN = "由经营者发起的全额批量退款";

    /**
     * 售后流水描述
     */
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_0 = "已进入退款审核中";//c端申请售后动作描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_1 = "俱乐部经营者已同意您的退款";//b端同意退款动作描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_2 = "俱乐部经营者未通过您的退款申请，拒绝理由:";//b端拒绝退款动作描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_3 = "用户取消退款";//c端取消申请售后动作描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_4 = "退款将在【审核通过】后的1-3个工作日内原路退回，请您及时关注账户变动。";//请求环迅受理退款成功描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_5 = "请联系俱乐部经营者";//请求环迅受理退款失败描述
    public static final String CLUB_REFUND_FLOW_DESCRIPTION_6 = "退款将在【发起退款】后的1-3个工作日内原路退回，请您及时关注账户变动。";//批量退款请求环迅受理退款成功描述

    /**
     * 语音播报参数
     */
    public static final Integer TYPE_FREE = 1; //免费订单
    public static final Integer TYPE_NO_PAY = 2; //未支付订单
    public static final Integer TYPE_PAYED = 3; //.已支付订单

    public static final String MESSAGE_FREE = "行装提醒：您的俱乐部有新订单了";
    public static final String MESSAGE_NO_PAY = "您有新的行装俱乐部订单";
    public static final String MESSAGE_PAYED = "行装提醒：您的俱乐部收到";

    public static final Integer IS_PLAY_NO = 0;//不播放
    public static final Integer IS_PLAY_YES = 1;//播放



    /**
     * 图片路径的参数
     */
    public static final String PIC_MODULE = "club"; //module
    public static final String PIC_ACT = "activity";//act
    public static final String PIC_ARTICLE = "article";//act
    public static final String PIC_ACT_ADVERT = "advert";//act
    public static final String PIC_ACT_GROUP_QR_CODE = "groupQrCode";//act
    public static final String PIC_ACT_VIDEO_COURSE= "videoCourse";//act

    /**
     * 排期费用类型
     */
    public static final int SCHEDULE_COST_TYPE_BASIC = 1; //基础费用
    /**
     * 排期实际价格类型
     */
    public static final int SCHEDULE_REAL_TYPE_NORMAL = 1; //正常价
    /**
     * 是否开启
     */
    public static final int OPEN_UNABLE = 0; //关闭
    public static final int OPEN_ABLE = 1;   //名开启
    /**
     * 是否能被删除
     */
    public static final int CAN_DELETE_UNABLE = 0; //不能被删除
    public static final int CAN_DELETE_ABLE = 1;   //能被删除
    /**
     * 是否选中
     */
    public static final int BOOLEAN_UNSELECTED = 0;  //未选中
    public static final int BOOLEAN_SELECTED = 1;    //已选中

    /**
     * 年龄类型
     */
    public static final int AGE_TYPE_UNSET = 0;     //未设置
    public static final int AGE_TYPE_CHILD = 1;     //儿童
    public static final int AGE_TYPE_ADULT = 2;     //成年
    public static final int AGE_TYPE_ILLEGAL = 3;  //不合法


    /**
     * 活动置顶数量
     */
    public static final int AC_STICK_TOP_LIMIT = 2; //活动置顶最大数量

    /**
     * 是否已删除
     */
    public static final int DELETED_NOT = 0;    //未删除
    public static final int DELETED_HAS = 1;    //已删除
    /**
     * 广告展示状态
     */
    public static final int ADVERT_STATUS_ALL =0;       //全部
    public static final int ADVERT_STATUS__NOT_START =1;//未开始
    public static final int ADVERT_STATUS_STARTING =2;  //展示中
    public static final int ADVERT_STATUS_PAUSE =3;     //已暂停
    public static final int ADVERT_STATUS_FINISHED =4;  //已结束


    /**
     * 退款说明类型
     */
    public static final int AC_REFUND_STAIR =0;        //阶梯条件退款
    public static final int AC_REFUND_COMPLETE =1;     //全额退款
    public static final int AC_REFUND_UN_SUPPORT =2;   //不支持退款
}
