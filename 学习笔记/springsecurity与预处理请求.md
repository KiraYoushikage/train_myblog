

# 解决方案

先说解决方案吧，我们代码里的解决方案：

```java
.antMatchers("/examRoom/find").permitAll()
.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  // 关键这行，就是允许Prefight预检请求
.antMatchers(HttpMethod.POST, "/login").permitAll()
```

　　Prefight预检请求就是一个 options 请求，我们可以点进去看源码：

```java
public static boolean isPreFlightRequest(HttpServletRequest request) {
    return HttpMethod.OPTIONS.matches(request.getMethod()) && request.getHeader("Access-Control-Request-Method") != null;
}
```

　　requestMatchers(CorsUtils::isPreFlightRequest).permitAll()的作用是将PreflightRequest不做拦截。 

### 一、为什么需要preflight request

　　我们都知道浏览器的同源策略，就是出于安全考虑，浏览器会限制从脚本发起的跨域HTTP请求，像XMLHttpRequest和Fetch都遵循同源策略。

　　浏览器限制跨域请求一般有两种方式：（1）浏览器限制发起跨域请求；（2）跨域请求可以正常发起，但是返回的结果被浏览器拦截了。

　　一般浏览器都是第二种方式限制跨域请求，那就是说请求已到达服务器，并有可能对数据库里的数据进行了操作，但是返回的结果被浏览器拦截了，那么我们就获取不到返回结果，这是一次失败的请求，但是可能对数据库里的数据产生了影响。

　　为了防止这种情况的发生，规范要求，对这种**可能对服务器数据产生副作用的HTTP请求方法，浏览器必须先使用OPTIONS方法发起一个预检请求，从而获知服务器是否允许该跨域请求：如果允许，就发送带数据的真实请求；如果不允许，则阻止发送带数据的真实请求**。

　　浏览器将CORS请求分成两类：简单请求和非简单请求。

　　浏览器对这两种请求的处理是不一样的，非简单请求的CORS请求，会在正式通信之前，增加一次HTTP查询请求，称为"预检"请求（preflight）。

### 二、简单请求与非简单请求

　　详见之前总结的这篇博客：[浅析http简单请求与复杂请求](https://www.cnblogs.com/goloving/p/14525157.html)

　　CORS请求，浏览器与服务器交互的过程，这上面的坑主要就是Preflight。如果我们的后台用了安全管理框架（比如Spring Security），并且没有对Preflight这个请求做出相应的处理，那么这个请求会导致权限管控失败（比如无法登录）。

　　因为Preflight不携带Cookie，即不携带JSESSIONID，因此Spring Security拦截器会认为你没有登录。

　　由于 Prefight 预检请求，实际就是一个 options 请求，所以我们也可以允许所有 options 请求，这样解决这个问题。

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
        //跨域请求会先进行一次options请求，允许 options 请求
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        ...
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

### 三、简单请求与非简单请求的请求流程

　　详细内容看这篇文档，官方描述：[Cross-Origin Resource Sharing (CORS)：https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)

1、简单请求

　　这在客户端和服务器之间执行简单的交换，使用 CORS 标头来处理权限：

![image-20210915215641252](E:\项目\train\myblog\学习笔记\assets\springsecurity与预处理请求.assets文件夹\image-20210915215641252.png)

2、预检请求

　　与[“简单请求”（上面讨论过）不同](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#simple_requests)，对于“预检”请求，浏览器首先使用该[`OPTIONS`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/OPTIONS)方法向另一个源上的资源发送 HTTP 请求，以确定实际请求是否可以安全发送。跨站点请求是这样预检的，因为它们可能会对用户数据产生影响。

![image-20210915215752029](E:\项目\train\myblog\学习笔记\assets\springsecurity与预处理请求.assets文件夹\image-20210915215752029.png)

分类: [SpringSecurity+Redis](https://www.cnblogs.com/goloving/category/1989517.html)

参考：[浅析SpringSecurity对跨域非简单请求的Prefight预检请求的处理：requestMatchers(CorsUtils::isPreFlightRequest).permitAll()、及简单请求与非简单请求的理解](https://www.cnblogs.com/goloving/p/14928094.html)