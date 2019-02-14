package com.example.boot.listener.springEvent;

import org.springframework.context.ApplicationEvent;

/**
 * 2019/2/14 16:18
 * 走路呼呼带风
 */
public class MyApplicationEvent extends ApplicationEvent{

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MyApplicationEvent(Object source) {
        super(source);
    }
}
