package com.example.boot.aspect;

/**
 * 2018/9/18 17:49
 * 走路呼呼带风
 */
public enum LogType {

    BEFORE(1),  //方法前生成日志
    AFTER(2),   //方法后生成日志
    AROUND(3),  //方法前后均生成日志
    EXCLUDE(4); //使RequestMapping和Impl实现类中的日志不生效

    private final int type;
    LogType(int type){
        this.type = type;
    }

    public int type(){
        return type;
    }

}
