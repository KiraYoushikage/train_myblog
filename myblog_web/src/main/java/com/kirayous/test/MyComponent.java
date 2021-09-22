package com.kirayous.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.test
 * @date 2021/7/20 0:43
 */


public class MyComponent {


    @Test
    public void test(){

        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encodePass="$2a$10$MtaLvmOSvuPqHplcLIZ1qOUgssdjvh9bq6nP9gq2Jm3ZVoN81POB2";
//        bCryptPasswordEncoder.upgradeEncoding(encodePass);

//        String res=bCryptPasswordEncoder.encode("123456");
        boolean res=bCryptPasswordEncoder.matches("123456","$2a$10$cYS3b2ye1Zoa72HUvBnijO8GVu/.wmDLt4FrZvnMOvDxyVibHVsUa");
        System.out.println(res);

    }

    @Test
    public void test2(){

        father test1=new son();
        test1.testG();

    }

}


