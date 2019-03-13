package com.example.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 2019/3/12 10:53
 * 走路呼呼带风
 */
@RestController
@Slf4j
public class UEditorController {

    /**
     * 百度编辑器上传图片和视频配置项
     * @return    HashMap
     */
    @RequestMapping(value="/config")
    public Map<String,Object> config() {
        Map<String,Object> config = new HashMap<>();

        //图片配置imageActionName
        config.put("imageActionName","uploadImage");        /* 执行上传图片的action名称 */
        config.put("imageFieldName","file");                /* 提交的图片表单名称 */
        config.put("imageMaxSize",2048000);                 /* 上传大小限制，单位B */
        String[] imageAllowFiles = new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp",".webp"};
        config.put("imageAllowFiles",imageAllowFiles);      /* 上传图片格式显示 */
        config.put("imageCompressEnable",true);             /* 是否压缩图片,默认是true */
        config.put("imageCompressBorder",1600);             /* 图片压缩最长边限制 */
        config.put("imageInsertAlign","none");              /* 插入的图片浮动方式 */
        config.put("imageUrlPrefix","");                    /* 图片访问路径前缀 */

        //视频配置
        config.put("videoActionName","uploadVideo");        /* 执行上传视频的action名称 */
        config.put("videoFieldName","file");                /* 提交的视频表单名称 */
        config.put("videoUrlPrefix","");                    /* 视频访问路径前缀 */
        config.put("videoMaxSize",30*1024*1024);            /* 上传大小限制，单位B，默认30MB */
        config.put("videoAllowFiles",new String[]{".mp4"}); /* 上传视频格式显示 */
        return config;
    }

}
