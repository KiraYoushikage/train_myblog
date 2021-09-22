# 问题





>描述：
>
>1.我想使用springsecurity的注销登陆操作
>
>配置代码如下：
>
>```java
>
>        http.formLogin().loginProcessingUrl("/login") 
>                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/toLoginOut","POST"));
>```
>
>这里我选择的接口是“/toLoginOut”,接受方法为“POST”请求
>
>并且我实现LogoutSuccesshandler，让他实现方法，返回json数据格式
>
>```java
>@Component
>public class MyLoginOutSuccessHandler implements LogoutSuccessHandler {
>    @Override
>    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
>
>        System.out.println("登出成功");
>        httpServletResponse.setContentType("application/json;charset=UTF-8");
>        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success().setMessage("注销成功！")));
>    }
>}
>```
>
>这里我将他注册到容器里面成为bean对象，
>
>本来以为已经万事俱备，只欠东风了。。。。。



# 现象

>
>
>我们登陆后台登陆成功后
>
>选择退出操作
>
>![image-20210915212240065](E:\项目\train\myblog\学习笔记\assets\有时候你就算注册成bean，某某某框架的配置也不一定起作用.assets文件夹\image-20210915212240065.png)
>
>前端发送Post请求，请求注销登陆
>
>```javascript
> // 调用注销接口
>        this.axios.post("/api/toLoginOut").then(value => console.log(value)).catch(reason => console.log(reason));
>```



>
>
>然而奇怪的事情发生了
>
>![image-20210915212413301](E:\项目\train\myblog\学习笔记\assets\有时候你就算注册成bean，某某某框架的配置也不一定起作用.assets文件夹\image-20210915212413301.png)
>
>console控制台报错
>
>而且他显示的是get的请求，更何况我请求的接口完全不是/login?logout，这实在是牛头不对马嘴
>
>我vue 的反向代理也是正常的
>
>```javascript
>devServer: {
>    proxy: {
>      "/api": {
>        target: "http://127.0.0.1:8000",
>        changeOrigin: true,
>        logLevel: "debug",
>        pathRewrite: {
>          "^/api": ""
>        }
>      }
>    },
>    
>    
>```
>
>

>
>
>查看网络请求
>
>![image-20210915212649531](E:\项目\train\myblog\学习笔记\assets\有时候你就算注册成bean，某某某框架的配置也不一定起作用.assets文件夹\image-20210915212649531.png)
>
>发现这类其实出现了我们toLoginOut请求，但是为什么又莫名出现了login？logout的请求?
>
>难道是后端给我们自动重定向了？
>
>

>
>
>![image-20210915212800689](E:\项目\train\myblog\学习笔记\assets\有时候你就算注册成bean，某某某框架的配置也不一定起作用.assets文件夹\image-20210915212800689.png)
>
>再来看看toLoginOut请求，发现状态码为302，
>
>302的意思就算资源暂时性转移，也就是所后端给我们重定向到登陆页面了？
>
>这又是为什么？



# 结论

>
>
>思来想去，最终怀疑我自定义的handler是不是没起效果
>
>我明明注册成bean了，按道理来说，springboot 的自动装配，应该会起效才对
>
>我试着在配置类中主动设置logoutSuccessHandler
>
>```java
>
>    @Autowired
>    MyLoginOutSuccessHandler myLoginOutSuccessHandler;
>  http.formLogin().loginProcessingUrl("/login") 
>                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/toLoginOut","POST")).logoutSuccessHandler(myLoginOutSuccessHandler); //这里主动添加handler
>```



>
>
>使用postman测试
>
>![image-20210915213246443](E:\项目\train\myblog\学习笔记\assets\有时候你就算注册成bean，某某某框架的配置也不一定起作用.assets文件夹\image-20210915213246443.png)
>
>发现成功得到结果

>
>
>所以证明的是，有些第三方框架设计的时候，你只要注入容器实现了他给的接口，就能够成为框架的一个组件起作用，
>
>但是有些内容是不起效的，需要严格按照文档进行使用。