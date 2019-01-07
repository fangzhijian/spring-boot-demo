package com.example.boot.errorCode;

/**
 * 俱乐部统一返回码
 * code范围 3000-3999
 */
public enum  ClubError{

    /**
     * 没有message的需要写注释提示,并在InfoJson里手动写message
     * 按照序号排序
     */
    C3000_START_ERROR(3000),

    C3001_ARTICLE_EMPTY(3001,"该文章不存在或无法访问"),

    C3002_CLUB_EMPTY(3002,"俱乐部不存在"),
    /**
     * 俱乐部群发功能被禁用
     */
    C3003_CLUB_FORBIDDEN(3003),

    C3004_CLUB_NONE_PUSH_LIMIT(3004,"俱乐部无可群发次数"),

    C3005_CLUB_NONE_PUSH_PEOPLES(3005,"俱乐部无可群发人数"),

    C3006_CLUB_NONE_PUBLIC_LIMIT(3006,"今日100万总群发次数已用完"),

    C3007_UPLOAD_FAILED(3007,"上传失败"),

    C3008_UPLOAD_NUM_ERROR(3008,"轮播图范围1-5张"),

    C3009_UPLOAD_MOVE_FAILED(3009,"图片移动失败"),

    C3010_ACTIVITY_EMPTY(3010,"活动不存在"),

    C3011_ACTIVITY_CANNOT_DELETE(3011,"活动中已存在订单无法被删除"),

    C3012_UPDATE_SOLD_OUT_FAILED(3012,"删除下架失败或活动不存在"),

    C3013_UPDATE_CLOSE_APPLY_FAILED(3013,"开启关闭活动报名失败或活动不存在"),

    C3014_UPDATE_STICK_TOP_FAILED(3014,"活动开启关闭置顶失败或活动不存在"),

    C3015_UPDATE_FAILED(3015,"修改失败"),

    C3016_DELETE_FAILED(3016,"删除失败"),

    C3017_CATEGORY_CANNOT_DELETE(3017,"分类标题已被使用不能进行编辑或删除"),
    /**
     * 排序号超出范围
     */
    C3018_CATEGORY_ORDER_OVER(3018),

    C3019_ACTIVITY_NONE_APPLY(3019,"还未有人报名活动哦"),

    C3020_ACTIVITY_NOT_PUBLIC(3020,"活动不是公开的"),

    C3021_UPLOAD_FAILED_QR_CODE(3021,"二维码上传失败"),

    C3022_ACTIVITY_IS_EXPIRED(3022,"活动已过期"),
    /**
     * 修改活动状态失败
     */
    C3023_ACTIVITY_UP_TYPE_FAILED(3023),

    C3024_ONLINE_MUSIC_FAILED(3024,"搜索在线歌曲功能发生了错误,我们会尽快修复"),




    C3976_CLUB__EXPORT_FAILED(3976,"导出excel失败"),

    C3977_CLUB_CANT_APPLY(3977,"当前状态不可申请售后"),

    C3978_CLUB_NO_REFUND(3978,"没有要处理的数据呢"),

    C3979_CLUB_APPLYLIST_EXPORT_ERRO(3979,"活动报名信息表导出失败"),

    C3980_CLUB_BATCH_DEAL_REDFUND_ERR(3980,"经营者发起批量售后操作异常"),

    C3981_CLUB_ORDER_AFTERMARKET_MONEY_LACK(3981,"经营者账户资金不足，请充值后再次操作退款"),

    C3982_CLUB_ORDER_APPLY_ACTIVITY_ERRSTATUS(3982,"活动状态不是公开的"),

    C3983_CLUB_CREATE_ORDER_WAIT(3983,"当前排期活动报名人数过多,请稍后再试"),

    C3984_CLUB_ORDER_URID_NO_EXIST(3984,"用户id未填写"),

    C3985_CLUB_ORDER_ISEMPTY(3985,"创建订单入参信息为空"),

    C3986_CLUB_REFUND_PRSENT_STATUS_ERROR(3986,"此订单未申请售后"),

    C3987_CLUB_ORDER_APPLY_PHONE_NO_EXIST(3987,"申请人手机号未填写"),

    C3988_CLUB_ORDER_APPLY_FREE_NO_EXIST(3987,"申请人手机号或昵称或者性别未填写"),

    C3989_CLUB_ORDER_ACTIVITY_SCHEDULE_NO_EXIST(3989,"所选择的排期数据不存在"),

    C3990_CLUB_ORDER_NO_EXIST(3990,"该订单不存在"),

    C3991_CLUB_APPLY_NONETITY_ERROR(3991,"查询的当前月份排期信息不存在"),

    C3992_CLUB_REFUND_APPLY_GET_DISTRIBUTED_LOCK_FAILED(3992,"用户申请售后获取分布式锁失败"),

    C3993_CLUB_REFUND_PRSENT_STATUS_ERROR(3993,"您当前的售后状态不是待处理售后0或98"),

    C3994_CLUB_REFUND_AGREE_GET_DISTRIBUTED_LOCK_FAILED(3994,"在经营者进行同意售后的操作中，获取分布式锁失败"),

    C3995_CLUB_ORDER_CANCEL_FAILED(3995,"取消订单失败"),

    C3996_CLUB_ORDER_APPLY_ACTIVITY_CLOSE(3996,"活动已关闭不可报名"),

    C3997_CLUB_ORDER_APPLY_CUTTIME(3997,"超过报名截止时间"),
    /**
     * 剩余可报名名额不足
     */
    C3998_CLUB_ORDER_APPLY_REMAIN(3998),

    C3999_END_ERROR(3999,"这是club项目的错误信息"),

    ;

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误信息
     */
    private String message;

    /**
     *
     * @return 返回错误码
     */
    public int code(){
        return code;
    }

    /**
     *
     * @return 返回错误信息
     */
    public String message(){
        return message;
    }

    /**
     *
     * @param code 错误码
     * @param message 错误信息
     */
    ClubError(int code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     *
     * @param code 错误码
     */
    ClubError(int code){
        this.code = code;
    }


}
