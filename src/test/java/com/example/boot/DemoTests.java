package com.example.boot;

import com.alibaba.fastjson.JSONObject;
import com.example.boot.model.Body;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
        String token = "21_dc6jECBTw9SYjgBUjC4dqATDJabkAv7PAdfj71aCIiIk6FEFqYZM4NpiwDseMVhxyJZbd4zUhLg9DnknuS1FbCoVVKKPSOckLVPp5dtdr0Pux7CoV5wItzRs41VEaR-twp4LnoDXjTb3pG0ePCWfAFAUHB";
        String url ="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token;
        List<Body> list;
        try {
            list = getListBody("C:/Users/fzj/Desktop/pic/shop.xlsx");
        } catch (IOException e) {
            log.error("读取excel异常,方法已结束");
            return;
        }
        int qrCodeWidth = 760;
        int qrCodeHeight = 760;
        for (Body body:list) {
            try {
                File pic = new File("C:/Users/fzj/Desktop/pic/template.png");
                BufferedImage template = ImageIO.read(pic);
                ByteArrayInputStream inputStream = createQrCode(body.getId(), url,qrCodeWidth);
                BufferedImage imageCode = ImageIO.read(inputStream);

                int textSize = 80;
                Graphics2D graphics2D = template.createGraphics();
                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, textSize));
                graphics2D.setColor(Color.GREEN);

                //计算位置
                int xLeft= (template.getWidth()-qrCodeWidth)/2; //300是二维码的宽度
                String text = body.getId().toString()+body.getName();
                int textLeft= (template.getWidth()-textSize*body.getId().toString().length()/2-textSize*body.getName().length())/2; //300是二维码的宽度

                graphics2D.drawImage(imageCode, xLeft, 720, qrCodeWidth, qrCodeHeight, null);
                graphics2D.drawString(text, textLeft, 600);
                graphics2D.dispose();
                ImageIO.write(template, "png", new File(String.format("C:/Users/fzj/Desktop/qrCode/2019-%s.png", body.getId())));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                log.error("商户id={}创建小程序二维码失败", body.getId());
            }
        }
    }


    private static ByteArrayInputStream createQrCode(Long id,String url,int width) throws IOException {
        JSONObject json = new JSONObject();
        json.put("page", "pages/index/index");//你二维码中跳向的地址
        json.put("scene", String.format("0,%s",id));//场景,用于接收参数,
        json.put("width", width);//图片大小
        json.put("is_hyaline",true); //是否生成透明底色
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

    private static List<Body> getListBody(String file) throws IOException {
        File excel = new File(file);
        FileInputStream fileInputStream = new FileInputStream(excel);

        boolean isExcel2003 = true;
        if (excel.getName().matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        Workbook workbook;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(fileInputStream);
        } else {
            workbook = new XSSFWorkbook(fileInputStream);
        }
        Sheet sheet = workbook.getSheetAt(0);
        int totalRow = sheet.getLastRowNum();
        log.info("总行数:{},开始导入数据到数据库",totalRow);

        List<Body> list = new ArrayList<>(totalRow);
        for (int i = 1; i <totalRow+1 ; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell cell1 =  row.getCell(0);
            Cell cell2 =  row.getCell(1);
            if (cell1 == null || cell2 == null){
                log.error("第{}行数据缺失",i);
            }else {
                Body body = new Body();
                System.out.println(cell1.getCellType());
                if (cell1.getCellType() == Cell.CELL_TYPE_STRING){
                    body.setId(new BigDecimal(cell1.getStringCellValue()).longValue());
                }else {
                    body.setId(new BigDecimal(cell1.getNumericCellValue()).longValue());
                }
                if (cell2.getCellType() == Cell.CELL_TYPE_STRING){
                    body.setName(cell2.getStringCellValue());
                }else {
                    body.setName(String.valueOf(cell2.getNumericCellValue()));
                }
                list.add(body);
            }

        }
        return list;
    }
}
