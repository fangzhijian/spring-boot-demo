package com.example.boot;

import com.alibaba.fastjson.JSONObject;
import com.example.boot.util.threadpool.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 2019/5/21 20:14
 * 走路呼呼带风
 */
@Slf4j
public class DemoTests {

    private static RestTemplate restTemplate;

    static {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.forEach((x) -> {
            if (x instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) x).setDefaultCharset(Charset.forName("UTF-8"));
            }
        });
    }

    public static void main(String[] args){
        String token = "21_i2lpHEfCdxUEQb8WZtrbsfl3XhXfgrazbI7odEvRoKzinZn-9SNK1RMWfBSq4wrnaRIhirkMhJUFZw9a2yLxdrZh5fa8nW7RyTRF9HAAQFLM8sfOK1jN9T6ZpnNubDOzJjeGOEVPJbqySFmgDGEiADAMPN";
        String url ="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token;
        List<Integer> list = new ArrayList<>();
        list.add(1);
        int qrCodeWidth = 800;
        for (Integer id:list) {
            try {
                ByteArrayInputStream inputStream = createQrCode(id, url,qrCodeWidth);
                File pic = new File("C:/Users/fzj/Desktop/pic/blueSky.jpg");
                BufferedImage template = ImageIO.read(pic);
                BufferedImage imageCode = ImageIO.read(inputStream);
                Graphics2D graphics2D = template.createGraphics();
//                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 30));
//                graphics2D.setColor(Color.BLACK);

                //计算位置
                int xLeft= (template.getWidth()-qrCodeWidth)/2; //300是二维码的宽度

                graphics2D.drawImage(imageCode, xLeft, 650, qrCodeWidth, 800, null);
//                graphics2D.drawString(id.toString()+"商家", 50, 100);
                graphics2D.dispose();
                ImageIO.write(template, "jpg", new File(String.format("C:/Users/fzj/Desktop/qrCode/2019-%s.jpg", id)));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                log.error("商户id={}创建小程序二维码失败", id);
            }
        }
    }


    private static ByteArrayInputStream createQrCode(Integer id,String url,int width) throws IOException {
        JSONObject json = new JSONObject();
        json.put("page", "pages/index/index");//你二维码中跳向的地址
        json.put("scene", String.format("0,%s",id));//场景,用于接收参数,
        json.put("width", width);//图片大小
        json.put("is_hyaline",false); //是否生成透明底色
        ResponseEntity<Resource> resourceResponseEntity = restTemplate.postForEntity(url,json.toJSONString() , Resource.class);
        //判断请求二维码过程中是否失败
        if (resourceResponseEntity.getBody() == null){
            throw new IOException();
        }
        InputStream inputStream =  resourceResponseEntity.getBody().getInputStream();
        byte[] data = toByteArray(inputStream);
        if (data == null ){
            throw new IOException();
        }
        String result = new String(data, Charset.forName("UTF-8"));
        if (result.contains("errcode")){
            throw new IOException();
        }

        return new ByteArrayInputStream(data);


    }


    /**
     * io 流得到String
     */
    private static byte[] toByteArray(InputStream inputStream){
        ByteArrayOutputStream output = new ByteArrayOutputStream();;
        try {
            byte[] buffer = new byte[4096];
            int n ;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                output.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
