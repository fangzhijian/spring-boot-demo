package com.example.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 2018/10/30 16:59
 * 走路呼呼带风
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@Nullable ViewControllerRegistry registry) {
        if (registry == null){
            return;
        }
        registry.addViewController("/index").setViewName("index");
    }

    @Override
    public void addResourceHandlers(@Nullable ResourceHandlerRegistry registry) {
        if (registry == null){
            return;
        }
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
