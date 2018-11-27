package com.example.boot;

import com.example.boot.util.HttpUtil;

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
        String path = "http://localhost:9000/efg?name=大壳&id=3&createTime="+new Date().toString();
        URL url = new URL(HttpUtil.getEncodeUrl(path));
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
        System.out.println(new String(outputStream.toByteArray(),"UTF-8"));
    }
}
