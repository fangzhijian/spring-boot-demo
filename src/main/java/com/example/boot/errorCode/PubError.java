package com.example.boot.errorCode;

/**
 * 公共返回码
 * code范围2001-2999
 * 2000为正确码
 */
public enum PubError {

    /**
     * 缺少入参
     * message在InfoJson里面填写缺少的哪个参数
     */
    P2001_PARAM_LACK(2001),
    /**
     * 入参验证错误
     * message在InfoJson里面填写哪个参数验证错误
     */
    P2002_PARAM_ERROR(2002),

    P2003_SYSTEM_EXCEPTION(2003,"an exception has occurred"),

    P2004_SYSTEM_TIMEOUT(2004,"连接超时"),

    P2005_NONE_AUTH(2005,"权限不足"),

    P2006_REPEAT_CLICK(2006,"操作频率过快,请稍后再试"),

    P2007_RESOURCE_CLICK(2007,"当前人数过多,稍后再试试看"),


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
    PubError(int code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     *
     * @param code 错误码
     */
    PubError(int code){
        this.code = code;
    }

}
