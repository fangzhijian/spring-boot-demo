package com.example.boot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * User: 走路呼呼带风
 * Date: 2018/11/27
 * Time: 21:15
 */
public class HttpUtil {

    public static String getEncodeUrl(String url) {
        String[] urlPath = url.split("\\?");
        StringBuilder finalUrl = new StringBuilder(urlPath[0]).append("?");
        if (urlPath.length > 1) {
            String[] parameters = urlPath[1].split("&");
            for (int i = 0; i < parameters.length; i++) {
                String[] keyValue = parameters[i].split("=");
                try {
                    finalUrl.append(URLEncoder.encode(keyValue[0], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                finalUrl.append("=");
                try {
                    finalUrl.append(URLEncoder.encode(keyValue[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (i != parameters.length - 1 && parameters.length > 1) {
                    finalUrl.append("&");
                }

            }
        }
        return finalUrl.toString();
    }
}
