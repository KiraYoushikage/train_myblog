package com.kirayous.handler.auth;

import com.alibaba.fastjson.JSON;
import com.kirayous.common.auth.MyUserDetails;
import com.kirayous.common.Result;
import com.kirayous.common.ResultInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.handler.auth
 * @date 2021/9/2 21:56
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
////        onAuthenticationSuccess(request,response,authentication);
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //验证成功来到这个处理器
        //然后获取用户信息
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.success()
                .setCode(ResultInfo.VERIFY_SUCCESS.getCode())
                .setMessage(ResultInfo.VERIFY_SUCCESS.getMessage()).setData(user)));

    }
}
