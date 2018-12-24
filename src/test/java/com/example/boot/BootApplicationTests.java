package com.example.boot;

import com.example.boot.config.BootApplication;
import com.example.boot.mapper.UserMapper;
import com.example.boot.model.Activity;
import com.example.boot.model.User;
import com.google.gson.Gson;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class BootApplicationTests {


	@Autowired
	private Gson gson;
	@Autowired
	private UserMapper userMapper;
	@Value("${body.name}")
	private String name;
	@Autowired
	private RestTemplate restTemplate;
	@Test
	public void contextLoads() throws SQLException {
		System.out.println(restTemplate.getForObject("http://localhost:8888/api/appManage/getInfo",String.class));
	}

	@Test
	public void viewCount(){
		//修改专线阅读量
		List<Activity> viewCountList = userMapper.getViewCountList();
		userMapper.updateViewCount(viewCountList);
		//文章总数sql
//		UPDATE js_club a SET cl_artno = (SELECT count(1) FROM js_article WHERE cl_id = a.id);
		//活动代理专线数
//		UPDATE js_club a SET cl_ac_ag_no = (SELECT count(1) FROM js_club_activity_list WHERE club_id = a.id and special_type = 2);
		//活动自营数
//		UPDATE js_club a SET cl_ac_self_no = (SELECT count(1) FROM js_club_activity_list WHERE club_id = a.id and special_type = 0);
		//活动总数
//		UPDATE js_club a SET cl_ac_no = (SELECT count(1) FROM js_club_activity_list WHERE club_id = a.id );
		//添加俱乐部的创建时间和完善时间
//		UPDATE js_club SET createtime = cl_regist;
//		UPDATE js_club SET perfect_time = cl_regist WHERE cl_idnumber IS NOT NULL;
		//设置之前的俱乐部身份默认为店主
//		UPDATE js_club SET member_type = 2 ;
	}

}
