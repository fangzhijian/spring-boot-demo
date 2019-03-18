package com.example.boot.util;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/**
 * 2019/2/28 16:52
 * 走路呼呼带风
 * 网易易盾签名
 */
public class SignatureUtils {

    /**
     * 生成签名信息
     * @param secretKey 产品私钥
     * @param params 接口请求参数名和参数值map，不包括signature参数名
     */
    public static String getSignature(String secretKey, Map<String, String> params){
        // 1. 参数名按照ASCII码表升序排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 2. 按照排序拼接参数名与参数值
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }
        // 3. 将secretKey拼接到最后
        sb.append(secretKey);

        // 4. MD5是128位长度的摘要算法，转换为十六进制之后长度为32字符
        return DigestUtils.md5Hex(sb.toString().getBytes(Charset.forName("UTF-8")));
    }
}
