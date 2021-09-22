package com.kirayous.handler.auth;

import com.alibaba.fastjson.JSON;
import com.kirayous.api.admin.service.IUserAuthService;
import com.kirayous.common.Result;
import com.kirayous.common.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.handler.auth
 * @date 2021/9/3 16:44
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private IUserAuthService userAuthService;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {


        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.error()
                .setCode(ResultInfo.LOGIN_FAILED.getCode())
                .setMessage(ResultInfo.LOGIN_FAILED.getMessage())));
//
//        System.out.println("登陆失败！！");

    }
}
