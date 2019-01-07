package com.example.boot.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * 2019/1/7 20:33
 * 走路呼呼带风
 * logback不同级别日志颜色配置
 * 条件pattern中使用 %highlight(),
 * 并加上标签 <conversionRule conversionWord="highlight" converterClass="com.example.boot.config.HighlightingConverter"/>
 */
public class HighlightingConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {
    @Override
    protected String getForegroundColorCode(ILoggingEvent iLoggingEvent) {
        //颜色数字查看类ANSIConstants
        switch (iLoggingEvent.getLevel().toInt()) {
            case Level.ERROR_INT:
                return "1;31";
            case Level.WARN_INT:
                return "1;33";
            case Level.INFO_INT:
                return "1;32";
            case Level.DEBUG_INT:
                return "1;37";
            default:
                return null;
        }
    }
}
