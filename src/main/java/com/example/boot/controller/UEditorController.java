package com.example.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 2019/3/12 10:53
 * 走路呼呼带风
 */
@RestController
@Slf4j
public class UEditorController {

    private final RedisTemplate<String,Object> redisTemplate;
    private final LettuceConnectionFactory lettuceConnectionFactory;


    public UEditorController(@Qualifier("redisTemplateDB3")RedisTemplate<String, Object> redisTemplate,
                             @Qualifier("lettuceConnectionDB3") LettuceConnectionFactory lettuceConnectionFactory) {
        this.redisTemplate = redisTemplate;
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

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
        config.put("imageMaxSize",1024*1024);               /* 上传大小限制，单位B */
        String[] imageAllowFiles = new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp",".webp"};
        config.put("imageAllowFiles",imageAllowFiles);      /* 上传图片格式显示 */
        config.put("imageCompressEnable",false);             /* 是否压缩图片,默认是true */
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

    /**
     * 富文本编辑器上传图片
     * @param file 图片文件
     * @return     RichEdit
     */
    @PostMapping("richImage")
    public Map<String,Object> richImage(@RequestParam MultipartFile file, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("title", file.getOriginalFilename());
        try {
            map.put("state", "SUCCESS");
            map.put("url", "http://res.outdoorclub.com.cn/p3/club/article/2018/7/17/6304230/54077/15320627454087621.gif");
        } catch (Exception e) {
            map.put("state","ERROR");

        }
        return map;
    }

    /**
     * 富文本编辑器上传视频
     * @param file  视频文件
     * @return      RichEdit
     */
    @PostMapping("richVideo")
    public Map<String,Object> richVideo(@RequestParam MultipartFile file,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("title", file.getOriginalFilename());
        try {
            map.put("state", "SUCCESS");
            map.put("url", "http://res.outdoorclub.com.cn/p3/club/videoCourse/2018/10/31/1/224618/15409971782003872.mp4");
        } catch (Exception e) {
            map.put("state","ERROR");
        }
        return map;
    }

    @GetMapping("test11")
    @ResponseBody
    public void test11(){
        redisTemplate.opsForValue().set("123","猪大肠");
        int database = lettuceConnectionFactory.getDatabase();
        System.out.println(database);
        LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        System.out.println(connectionFactory.getDatabase());
        System.out.println(connectionFactory == lettuceConnectionFactory);

    }

}
