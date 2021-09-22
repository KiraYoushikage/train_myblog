package com.kirayous.admin.controller;

import com.kirayous.api.admin.service.impl.TestServiceImpl;
import com.kirayous.api.service.MyRedisService;
import com.kirayous.common.Result;
import com.kirayous.test.MyComponent2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.admin.controller
 * @date 2021/7/16 2:38
 */
@RestController
@RequestMapping("/get")
@Api("测试接口")
@Slf4j
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    @Autowired
    MyRedisService redisService;
//    @Autowired
//    RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
//        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        redisTemplate.opsForValue().set("xxx","test");
//        redisTemplate.get
        redisService.set("test","this is a test");
       String res=(String) redisService.get("test");
        System.out.println(res);
        return res;
    }


//    @GetMapping("/test/{message}")
//    public Result testGet(@PathVariable(value = "message",required = false) String message)
//    {
//
//
//        if (!StringUtils.isEmpty(message)){
//            return Result.success().setMessage(testService.test());
//
//        }else
//            throw new RuntimeException(message);
//    }
//
//    @GetMapping("/test2")
//    @ApiOperation("test2")
//    public Result testGet2()
//    {
//        return null;
//    }
//
//    @GetMapping("/test1")
//    @ApiOperation("test1")
//    public String testGet1()
//    {
//
//        MyComponent2 myComponent2=new MyComponent2();
//        return "testGet1";
//    }
//
//    @GetMapping("/getAuthorities")
//    @ApiOperation("测试用来获取使用SpringSecurity框架的当前登陆用户的权限")
//    public Result getAuthroities(){
//
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//
//        AbstractAuthenticationToken test=(AbstractAuthenticationToken)authentication;
//
//
//
//
//        Map<String ,Object> map=new HashMap<>();
//
//        map.put("principal",test.getPrincipal());
//        map.put("authorities",test.getAuthorities());
//        map.put("credentials",test.getCredentials());
//        map.put("details",test.getDetails());
//
//        return  Result.success(200,"test",map);
////        log.debug(test.getPrincipal().toString());
////        log.debug(test.getAuthorities().toString());
////        log.debug(test.getCredentials().toString());
////        return test.getPrincipal();
//        //return (String) test.getDetails();//Details就类似于MyUserDetails里面的Authorities内容
//        /*返回结果如下所示*/
//        /*
//        {
//            "status":false,
//             "code":400,
//             "message":"org.springframework.security.web.authentication.WebAuthenticationDetails cannot be cast to java.lang.String",
//             "data":null
//             }
//            */
//
//    }


}
