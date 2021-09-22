![image-20210910194931532](E:\项目\train\myblog\学习笔记\encode密码验证问题.assets\image-20210910194931532.png)



# 1.BCryptPasswordEncoder 问题（待解决）

springsecurity添加BCryptPasswordEncoder加密器

```java
/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.config
 * @date 2021/8/23 16:02
 */
@Configuration
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
    @Override
    public void configure(WebSecurity web) throws Exception {


        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.formLogin().loginProcessingUrl("/login").permitAll();
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        //放行掉这个iframe加载
        http.headers().frameOptions().disable();
        http.csrf().disable().exceptionHandling();
;

        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    //用户密码加密验证,使用我们注入的BCryptPasswordEncoder进行加密
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService( myUserDetailsService).passwordEncoder(passwordEncoder());//这里passwordEncoder获取到的是容器中同一个对象，这个其实由spring@Configuragtion的实现原理有关
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
    public BCryptPasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
//@Bean
//public PasswordEncoder passwordEncoder() {
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//}



}

```





使用encode加密密码然后更新数据库

```java
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
    BCryptPasswordEncoder encoder;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {


        UserAuth userAuth=new UserAuth();
        userAuth.setId(1);
        userAuth.setPassword(encoder.encode("123456"));
        boolean res=userAuthService.lambdaUpdate().update(userAuth);
        System.out.println("更新用户：admin + res："+res);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.error()
                .setCode(ResultInfo.LOGIN_FAILED.getCode())
                .setMessage(ResultInfo.LOGIN_FAILED.getMessage())));
//
//        System.out.println("登陆失败！！");

    }
}

```



>问题描述：
>
>1.注入到spring容器中
>
>2.依赖注入encoder
>
>3.使用依赖注入的encode加密密码
>
>4.加密密码更新到数据库之后
>
>5.使用这个加密过后的密码登陆却提示密码验证错误
>
>并且console控制台会出现如下warn提示：
>
>```bash
>2021-09-10 19:45:49.727  WARN 16624 --- [nio-8000-exec-2] o.s.s.c.bcrypt.BCryptPasswordEncoder     : Encoded password does not look like BCrypt
>```
>
>同样，你不适用spring容器里面的encode对象，自己new一个新的对象，也会出现该问题。真心不知道什么原理，按理来说，使用同一个对象进行加密解密之后得到的结果应该都是一样的。很奇怪。





>
>
>但是使用注入到容器里面的encoder 去matches  数据库加密过后的password 返回值倒是true，不知道为什么springsecurity就是判断不到
>
>```java
> System.out.println(user);
>
>
>        System.out.println(encoder.matches("123456",user.getPassword()));
>
>```
>
>![image-20210910202259424](E:\项目\train\myblog\学习笔记\encode密码验证问题.assets\image-20210910202259424.png)





>
>
>网上找了很多方法，也没什么卵用，个人觉得是版本更新的问题，做该blog项目使用的springboot parent 版本为2.1.x，但是大佬源代码使用的是2.4.x版本的