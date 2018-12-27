package com.example.boot.config;

import com.example.boot.mapper.UserMapper;
import com.example.boot.model.Body;
import com.example.boot.model.ExcelData;
import com.example.boot.model.User;
import com.example.boot.util.ExcelUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.outdoor.club.model.admin.ParamConfig;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/10/10 14:21
 * 走路呼呼带风
 */
@Controller
@Slf4j
@Validated
public class IndexController {

    @Autowired
    public Body body;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


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
    @Cacheable(cacheNames = "user",key = "#id")
    public User test(Integer id){
        log.info(body.getName());
        User user = new User();
        user.setAge(id);
        userMapper.updateUser(user);
        log.info(user.getAge().toString());
        return userMapper.getById(id);
    }

    @RequestMapping("/test2")
    public void test2(HttpServletResponse response){

        List<User> list= new ArrayList<>();
        User user1 = new User(1,"小明","2018-12-10 12:12:12",10);
        User user2 = new User(2,"中明","2018-12-11 12:12:12",11);
        User user3 = new User(3,"大明","2018-12-12 12:12:12",12);
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
        redirectAttributes.addAttribute("createTime",new Date().toString());
        return "redirect:efg";
    }


    @RequestMapping(value = "upload",method =RequestMethod.POST)
    @ResponseBody
    public Integer upload(@RequestParam MultipartFile groupQrCode,
                          @NotNull @Range(min = 3,max = 6) Integer id,
                          @NotBlank @Size(max = 3) String name
                      ,@RequestParam String[] strings
                           ){
        if (groupQrCode != null){
            System.out.println(groupQrCode.isEmpty());
        }
        return id;
    }


}
