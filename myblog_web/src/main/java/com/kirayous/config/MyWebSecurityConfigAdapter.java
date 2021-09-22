package com.kirayous.config;

import com.kirayous.auth.MyUserDetailsService;
import com.kirayous.auth.MyUsernamePasswordAuthenticationFilter;
import com.kirayous.handler.auth.MyAuthenticationEntryPoint;
import com.kirayous.handler.auth.MyAuthenticationFailureHandler;
import com.kirayous.handler.auth.MyAuthenticationSuccessHandler;
import com.kirayous.handler.auth.MyLoginOutSuccessHandler;
import com.kirayous.test.MyBCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.config
 * @date 2021/8/23 16:02
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class MyWebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

//    @Autowired
//    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter;

    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Autowired
    MyLoginOutSuccessHandler myLoginOutSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {


        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        //放行掉这个iframe加载
        http.headers().frameOptions().disable();
        //如果开启csrf防护，则有些请求只能接受post请求，例如loginout请求本来是get请求，但是开启了csrf后，变为post请求才能接受
        //这里是设置了关闭CSRF防护
        http.csrf().disable().authorizeRequests().antMatchers("/login", "/session/invalid", "/toLoginOut").permitAll();
        http.formLogin().loginProcessingUrl("/login") //这个登出功能有bug，目前认为是前端无状态，选着登出的时候认为你是未登陆的状态
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/toLoginOut","POST")).logoutSuccessHandler(myLoginOutSuccessHandler);

//        关键这行，就是允许Prefight预检请求
        //        什么是（cors 预检请求） 就是你要跨域请求得时候 你要预先发一个请求看对面是拦你还是放你
        //解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        //未登录处理
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint);

//        http.csrf().disable().exceptionHandling();



        //把这个springsecurity框架自带的filter替换掉
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    //用户密码加密验证,使用我们注入的BCryptPasswordEncoder进行加密
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService( myUserDetailsService);

    }

    @Bean //这里出现bean的循环依赖导致注入失败的问题了
    public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {


        MyUsernamePasswordAuthenticationFilter filter=new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;

    }

    @Bean
//    @Lazy
    public BCryptPasswordEncoder passwordEncoder(){
        return  new MyBCryptPasswordEncoder();
    }
//@Bean
//public PasswordEncoder passwordEncoder() {
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//}



}
