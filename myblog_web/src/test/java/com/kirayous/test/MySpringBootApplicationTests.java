package com.kirayous.test;

import com.kirayous.MySpringBootApplication;
import com.kirayous.api.service.impl.RedisServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.test
 * @date 2021/8/20 8:45
 */



@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class MySpringBootApplicationTests {

    @Value("${enable.swagger2}")
    boolean enableSwagger2;
    @Autowired
    RedisServiceImpl redisService;
    @Test
    public void test1(){

        System.out.println(enableSwagger2);
    }

    @Test
    public void test2(){
//
//        redisService.set("xxx","this is a test");
    }
}
