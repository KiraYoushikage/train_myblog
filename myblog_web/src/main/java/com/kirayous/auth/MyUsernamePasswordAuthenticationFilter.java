package com.kirayous.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.auth
 * @date 2021/8/31 21:16
 */
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {



        //携带头要是json格式得application/json;charset=UTF-8
        //前后端分离 前端发送过来是json格式，这里是接收的是json串
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){

            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());}

            Map<String,String> map=null;
            ObjectMapper mapper=new ObjectMapper();
            //try(这里写入in流){}catch(){}这种写法很可以不用自己手动关闭流，当代码块执行完毕自动会关闭流
            try(InputStream in =request.getInputStream()){

                map=mapper.readValue(in,Map.class);
                System.out.println(map.toString());

            }catch(IOException e){
                e.printStackTrace();
            }
            if(!map.isEmpty()){

                String username=map.get("username");
                String password=map.get("password");

//                System.out.println(username);
//                System.out.println(password);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username!=null?username.trim():"", password!=null?password.trim():"");
                setDetails(request, authRequest);

                //调用getAuthenticationManager().authenticate(authRequest)对用户密码的正确性进行验证，认证失败就抛出异常，成功就返回Authentication对象。
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        //这里循环递归调用了如，如果前面条件出错，则会出现死循环
//        return this.attemptAuthentication(request,response);
        return this.getAuthenticationManager().authenticate(null);

    }
}
