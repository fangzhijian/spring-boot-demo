package com.example.boot;

import com.example.boot.model.User;
import com.example.boot.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * 2018/10/10 16:40
 * 走路呼呼带风
 */
public class Test {
    public static void main(String[] args) throws Exception{
        User user = new User();
        user.setId(2);
        user.setName("大壳");
//        user.setCreateTime(new Date());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String userJson = gson.toJson(user);
        System.out.println(userJson);
//        String path = "http://localhost:8080/index?user="+gson.toJson(user);
        String path = "http://localhost:8888/api/articlelist?clId=1099&sizePerPage=10&actType=list&arIssue=1&nowPageNo=1";
//        String path = "http://localhost:9000/efg?name=大壳&id=3&createTime="+new Date().toString();
//        String path = "http://localhost:9000/hij?userJson="+userJson;
        URL url = new URL(HttpUtil.getEncodeUrl(path));
        long start = System.currentTimeMillis();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int n;
        while ((n=inputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,n);
        }
        System.out.println(String.format("耗时%s",System.currentTimeMillis()-start));
        System.out.println(new String(outputStream.toByteArray(),"UTF-8"));

    }
}
