package com.example.boot;

import com.example.boot.config.BootApplication;
import com.example.boot.mapper.UserMapper;
import com.example.boot.model.User;
import com.google.gson.Gson;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.sql.SQLException;
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
	@Test
	public void contextLoads() throws SQLException {
		System.out.println(name);
		ExecutorService executorService = Executors.newScheduledThreadPool(5);
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println(2342);
			}
		});

	}

}
