package com.example.boot.controller;

import com.example.boot.aspect.LogType;
import com.example.boot.aspect.MethodLog;
import com.example.boot.aspect.RepeatLock;
import com.example.boot.aspect.ResourceLock;
import com.example.boot.errorCode.PubError;
import com.example.boot.exception.BusinessException;
import com.example.boot.listener.springEvent.MyApplicationEvent;
import com.example.boot.mapper.UserMapper;
import com.example.boot.model.ExcelData;
import com.example.boot.model.InfoJson;
import com.example.boot.model.User;
import com.example.boot.service.UserService;
import com.example.boot.service.impl.UserService2Impl;
import com.example.boot.util.ExcelUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.outdoor.club.controller.annualData.AnnualData;
import com.outdoor.club.model.admin.ParamConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/10/10 14:21
 * 走路呼呼带风
 */
@Controller
@Slf4j
@Validated
public class IndexController {


    private final UserService userService;
    private final Gson gson;
    private final UserMapper userMapper;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ApplicationContext applicationContext;

    public IndexController(@Qualifier("userService2")UserService userService, Gson gson, UserMapper userMapper, RedisTemplate<String, Object> redisTemplate, ApplicationContext applicationContext) {
        this.userService = userService;
        this.gson = gson;
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
        this.applicationContext = applicationContext;
    }


    @GetMapping("test2")
    @ResponseBody
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User test2(Integer id, HttpServletRequest request) throws InterruptedException {

        System.out.println(userService.getUser(2));
//        System.out.println(userMapper.getById(2));
//        Thread.sleep(3000);
//        System.out.println("=======");
//        System.out.println(userMapper.getById(1));
        final String s = UserService2Impl.stringValueResolver.resolveStringValue("${baseUrl}");
        System.out.println("||||"+s);
        return new User().setName("猪大肠");
    }

    @PostMapping("paramConfig")
    @ResponseBody
    public void paramConfig(String paramConfig){
        List<ParamConfig> paramConfigs = gson.fromJson(paramConfig,
                new TypeToken<List<ParamConfig>>(){}.getType());
        System.out.println(paramConfigs.size());
        Map<String,ParamConfig> map = new HashMap<>();
        for (ParamConfig param:paramConfigs){
            map.put(param.getKey().toString(),param);
        }
        redisTemplate.opsForHash().putAll("club:manage:config",map);
    }

    @PostMapping("appConfig")
    @ResponseBody
    public void appConfig(String appConfig){
        Map<Object,Object> map = gson.fromJson(appConfig,
                new TypeToken<Map<Object,Object>>(){}.getType());
        System.out.println(map);
        redisTemplate.opsForHash().putAll("club:app:manage",map);
    }

    @GetMapping("/test")
    @ResponseBody
    @Transactional
    @MethodLog(LogType.BEFORE)
    @RepeatLock(value = "#id",expireTime = 8000)
    @ResourceLock(value = "#id",expireTime = 8000)
//    @RepeatLock(value = "123",deleteFinish = true,expireTime = 5000)
//    @RepeatLock(value = "#u.name",prefixKey = "order:",expireTime = 3,timeUnit = TimeUnit.SECONDS)
//    @Cacheable(cacheNames = "user",key = "#id")
    public InfoJson test(Integer id, User u){
        log.debug("debug");
        log.info(u.toString());
        User user = new User();
        user.setAge(id);
        userMapper.updateUser(user);
        log.warn(user.getAge().toString());
        log.error("error");
        User byId = userMapper.getById(id);
        byId.setId(null);
        return InfoJson.getSucc(byId);
    }

    @RequestMapping("/test2")
    public void test2(HttpServletResponse response){

        List<User> list= new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        Date now = new Date();
        User user1 = new User(1,"小明",localDateTime,10,now);
        User user2 = new User(2,"中明",localDateTime,11,now);
        User user3 = new User(3,"大明",localDateTime,12,now);
        list.add(user1);
        list.add(user2);
        list.add(user3);
        ExcelData data = new ExcelData();
        String[] columnNames = {"排序","主键","姓名","创建时间"};
        data.setColumnNames(columnNames);

        List<List<Object>> rows = new ArrayList<>();

        for (int i = 0; i <list.size() ; i++) {
            List<Object> row = new ArrayList<>();
            User user = list.get(i);
            row.add(i+1);
            row.add(user.getId());
            row.add(user.getName());
            row.add(user.getCreateTime());
            rows.add(row);
        }
        data.setRows(rows);
        try{
            ExcelUtils.exportExcel(response,"abc",data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @RequestMapping("userCD")
    @ResponseBody
    public User userAbc(@Validated User user,String json,
                        @NotBlank(message = "年轻人啊年轻人") String address, @Size(min = 3,max = 5) String abc){
        System.out.println(user);
        System.out.println(json);
        return userMapper.getById(1);
    }

    @GetMapping("abc")
    public String abc(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("id",3);
        redirectAttributes.addAttribute("name","猪大肠");
        redirectAttributes.addAttribute("createTime",new Date());
        return "redirect:efg";
    }

    @GetMapping("efg")
    @ResponseBody
    public String efg(String id,String name,Date createTime){
        log.info("id,{}",createTime);
        log.info("我叫猪小明");
        return name;
    }


    @RequestMapping(value = "upload",method =RequestMethod.POST)
    @ResponseBody
    public InfoJson upload(@RequestParam MultipartFile file) throws IOException, BusinessException {
        if (file == null || file.isEmpty()){
            return InfoJson.setFailed(PubError.P2001_PARAM_LACK.code(),"请选择一个excel上传");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$"))) {
            throw new BusinessException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream inputStream = file.getInputStream();
        Workbook workbook;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheetAt(0);
        int totalRow = sheet.getLastRowNum();
        log.info("总行数:{},开始导入数据到数据库",totalRow);
        for (int i = 1; i <totalRow+1 ; i++) {
            Row row = sheet.getRow(i);
            if (row == null){
                continue;
            }
            AnnualData data = new AnnualData();
            Cell cell1 =  row.getCell(0);
            if (cell1 == null){
                continue;
            }
            Integer userId = Double.valueOf(cell1.getNumericCellValue()).intValue();
            data.setUserId(userId);

            Cell cell2 =  row.getCell(1);
            if (cell2 != null && cell2.getCellType() == Cell.CELL_TYPE_STRING){
                data.setPortrait(cell2.getStringCellValue());
            }

            Cell cell3 =  row.getCell(2);
            if (cell3 != null){
                if (cell3.getCellType() == Cell.CELL_TYPE_STRING){
                    data.setNickName(cell3.getStringCellValue());
                }else if (cell3.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    data.setNickName(String.valueOf(cell3.getNumericCellValue()));
                }
            }
            Cell cell4 =  row.getCell(3);
            if (cell4 != null){
                data.setCreateTime(cell4.getDateCellValue());
            }
            Cell cell5 =  row.getCell(4);
            if (cell5 != null){
                data.setUpgradeTime(cell5.getDateCellValue());
            }
            Cell cell6 =  row.getCell(5);
            if (cell6 != null){
                data.setSignInNum(Double.valueOf(cell6.getNumericCellValue()).intValue());
            }
            Cell cell7 =  row.getCell(6);
            if (cell7 != null){
                data.setSignIntegral(Double.valueOf(cell7.getNumericCellValue()).intValue());
            }
            Cell cell8 = row.getCell(7);
            if (cell8 != null){
                data.setShopPercent(new BigDecimal(cell8.getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }else {
                data.setShopPercent(new BigDecimal(23.66).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }
            Cell cell9 = row.getCell(8);
            if (cell9 != null){
                data.setFansNum(Double.valueOf(cell9.getNumericCellValue()).intValue());
            }
            Cell cell10 = row.getCell(9);
            if (cell10 != null){
                data.setOrderNum(Double.valueOf(cell10.getNumericCellValue()).intValue());
            }
            Cell cell11 = row.getCell(10);
            if (cell11 != null){
                data.setSaleroom(new BigDecimal(cell11.getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }
            Cell cell12 = row.getCell(11);
            if (cell12 != null){
                data.setAnnualIntegral(Double.valueOf(cell12.getNumericCellValue()).intValue());
            }
            Cell cell13 = row.getCell(12);
            if (cell13 != null){
                data.setCreateClubTime(cell13.getDateCellValue());
            }
            Cell cell14 = row.getCell(13);
            if (cell14 != null){
                data.setArticleNum(Double.valueOf(cell14.getNumericCellValue()).intValue());
            }
            Cell cell15 = row.getCell(14);
            if (cell15 != null){
                data.setArticleView(Double.valueOf(cell15.getNumericCellValue()).intValue());
            }
            Cell cell16 = row.getCell(15);
            if (cell16 != null){
                data.setActivityNum(Double.valueOf(cell16.getNumericCellValue()).intValue());
            }
            Cell cell17 = row.getCell(16);
            if (cell17 != null){
                data.setActivityView(Double.valueOf(cell17.getNumericCellValue()).intValue());
            }
            Cell cell18 = row.getCell(17);
            if (cell18 != null){
                data.setActivityApplyNum(Double.valueOf(cell18.getNumericCellValue()).intValue());
            }
            Cell cell19 = row.getCell(18);
            if (cell19 != null){
                data.setInviteShop(Double.valueOf(cell19.getNumericCellValue()).intValue());
            }
            Cell cell20 = row.getCell(19);
            if (cell20 != null){
                data.setShopIntegral(Double.valueOf(cell20.getNumericCellValue()).intValue());
            }
            Cell cell21 = row.getCell(20);
            if (cell21 != null){
                data.setTopSaleDay(cell21.getStringCellValue());
            }
            Cell cell22 = row.getCell(21);
            if (cell22 != null){
                data.setTopSaleroom(new BigDecimal(cell22.getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }
            Cell cell23 = row.getCell(22);
            if (cell23 != null){
                data.setTopOrderNum(Double.valueOf(cell23.getNumericCellValue()).intValue());
            }
            Cell cell24 = row.getCell(23);
            if (cell24 != null){
                data.setMemberType(Double.valueOf(cell24.getNumericCellValue()).intValue());
            }
            Cell cell25 = row.getCell(24);
            if (cell25 != null){
                data.setSaleroomPercent(new BigDecimal(cell25.getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }else {
                data.setSaleroomPercent(new BigDecimal("32.28").setScale(2,BigDecimal.ROUND_HALF_EVEN));
            }
            Cell cell26 = row.getCell(25);
            if (cell26 != null){
                data.setLabel(Double.valueOf(cell26.getNumericCellValue()).intValue());
            }
            log.info(data.toString());
            redisTemplate.opsForValue().set(String.format("annualData:%s",userId),data);
        }
        return InfoJson.getSuccess();
    }

    @RequestMapping("test5")
    @ResponseBody
    public User test5(@RequestBody User user){
        return user;
    }

    @RequestMapping("test6")
    @ResponseBody
    public User test6(String name){
        User user = new User().setCreateTime(LocalDateTime.now()).setName(name);
        userMapper.insertUser(user);
        return user;
    }

    @RequestMapping("test7")
    @ResponseBody
    public Date test7(Date date,@RequestParam LocalDateTime dateTime){
        System.out.println(dateTime);
        return date;
    }

    @RequestMapping("test8")
    @ResponseBody
    public Long test8() throws BusinessException {
        Instant instant = Instant.now();
        try {
            User user = userService.getUser(1);
            log.info(user.toString());
        }catch (Exception e){
            throw new BusinessException(500,"年轻人好生心浮气躁");
        }
        return Duration.between(instant,Instant.now()).toMillis();
    }

    @RequestMapping("test9")
    @ResponseBody
    @Transactional
    public void test9(Integer age){
        User user = new User();
        user.setId(1);
        user.setAge(age);
        userMapper.updateUser(user);
        applicationContext.publishEvent(new MyApplicationEvent(user));
    }
}
