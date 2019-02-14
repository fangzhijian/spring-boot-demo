package com.example.boot.listener;

import com.example.boot.listener.springEvent.MyApplicationEvent;
import com.example.boot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 2019/2/14 16:38
 * 走路呼呼带风
 * spring事件监听器
 * 默认监听器 SimpleApplicationEventMulticaster
 * 事件监听器具有事物,会遵循触发监听事件的事物
 *  SimpleApplicationEventMulticaster可以设置为异步方法
 *      查看方法 setTaskExecutor 和 multicastEvent
 * @see org.springframework.context.event.SimpleApplicationEventMulticaster
 *
 */
@Component
@Slf4j
public class SpringEventListener {

    @EventListener(classes = {MyApplicationEvent.class})
    public void listener(MyApplicationEvent event){
        log.info("spring MyApplicationEvent 开车了");
        log.info(event.getSource().toString());
    }

    @EventListener(classes = {ApplicationEvent.class})
    public void listener(ApplicationEvent event){
        Object source = event.getSource();
        if (source instanceof User){
            log.info("spring ApplicationEvent 开车了");
            log.info(event.getSource().toString());
        }
    }

}
