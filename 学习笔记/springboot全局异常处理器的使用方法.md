

# 前言（参考，虽然内容比较久，但是原理都是差不都的）

今天来一起学习一下Spring Boot中的异常处理，在日常web开发中发生了异常，往往是需要通过一个统一的异常处理来保证客户端能够收到友好的提示。

# 正文





本篇要点如下

- 介绍Spring Boot默认的异常处理机制
- 如何自定义错误页面
- 通过@ControllerAdvice注解来处理异常

## 介绍Spring Boot默认的异常处理机制

默认情况下，Spring Boot为两种情况提供了不同的响应方式。

一种是浏览器客户端请求一个不存在的页面或服务端处理发生异常时，一般情况下浏览器默认发送的请求头中Accept: text/html，所以Spring Boot默认会响应一个html文档内容，称作“Whitelabel Error Page”。



![img](https:////upload-images.jianshu.io/upload_images/5811881-810607573a1e18f2.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

image.png

另一种是使用Postman等调试工具发送请求一个不存在的url或服务端处理发生异常时，Spring Boot会返回类似如下的Json格式字符串信息



```json
{
    "timestamp": "2018-05-12T06:11:45.209+0000",
    "status": 404,
    "error": "Not Found",
    "message": "No message available",
    "path": "/index.html"
} 
```

原理也很简单，Spring Boot 默认提供了程序出错的结果映射路径/error。这个/error请求会在BasicErrorController中处理，其内部是通过判断请求头中的Accept的内容是否为text/html来区分请求是来自客户端浏览器（浏览器通常默认自动发送请求头内容Accept:text/html）还是客户端接口的调用，以此来决定返回页面视图还是 JSON 消息内容。
 相关BasicErrorController中代码如下：



![img](https:////upload-images.jianshu.io/upload_images/5811881-08aef117e11c5082.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

image.png

## 如何自定义错误页面

好了，了解完Spring Boot默认的错误机制后，我们来点有意思的，浏览器端访问的话，任何错误Spring Boot返回的都是一个`Whitelabel Error Page`的错误页面，这个很不友好，所以我们可以自定义下错误页面。

1、先从最简单的开始，直接在`/resources/templates`下面创建error.html就可以覆盖默认的`Whitelabel Error Page`的错误页面，我项目用的是thymeleaf模板，对应的error.html代码如下：

![img](https:////upload-images.jianshu.io/upload_images/5811881-1582a294e27f98da.png?imageMogr2/auto-orient/strip|imageView2/2/w/396/format/webp)

image.png





```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
动态error错误页面
<p th:text="${error}"></p>
<p th:text="${status}"></p>
<p th:text="${message}"></p>
</body>
</html>
```

这样运行的时候，请求一个不存在的页面或服务端处理发生异常时，展示的自定义错误界面如下：



![img](https:////upload-images.jianshu.io/upload_images/5811881-b04596967cf2954c.png?imageMogr2/auto-orient/strip|imageView2/2/w/668/format/webp)

image.png

2、此外，如果你想更精细一点，根据不同的状态码返回不同的视图页面，也就是对应的404，500等页面，这里分两种，错误页面可以是静态HTML（即，添加到任何静态资源文件夹下），也可以使用模板构建，文件的名称应该是确切的状态码。

- 如果只是静态HTML页面，不带错误信息的，在resources/public/下面创建error目录，在error目录下面创建对应的状态码html即可 ，例如，要将404映射到静态HTML文件，您的文件夹结构如下所示：

  ![img](https:////upload-images.jianshu.io/upload_images/5811881-85e5861fa03cc934.png?imageMogr2/auto-orient/strip|imageView2/2/w/526/format/webp)

  image.png

静态404.html简单页面如下：



```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    静态404错误页面
</body>
</html>
```

这样访问一个错误路径的时候，就会显示`静态404错误页面`错误页面

![img](https:////upload-images.jianshu.io/upload_images/5811881-de35ca1097783615.png?imageMogr2/auto-orient/strip|imageView2/2/w/610/format/webp)

image.png



> 注：这时候如果存在上面第一种介绍的error.html页面，则状态码错误页面将覆盖error.html，具体状态码错误页面优先级比较高。

- 如果是动态模板页面，可以带上错误信息，在

  ```
  resources/templates/
  ```

  下面创建error目录，在error目录下面命名即可:

  ![img](https:////upload-images.jianshu.io/upload_images/5811881-3d3af499382c0730.png?imageMogr2/auto-orient/strip|imageView2/2/w/554/format/webp)

  image.png

这里我们模拟下500错误，控制层代码,模拟一个除0的错误：



```java
@Controller 
public class BaseErrorController extends  AbstractController{ 
private Logger logger = LoggerFactory.getLogger(this.getClass()); 

    @RequestMapping(value="/ex") 
    @ResponseBody 
    public String error(){ 
        int i=5/0; 
        return "ex"; 
    } 
} 
```

500.html代码:



```xml
<!DOCTYPE html> 
<html xmlns:th="http://www.thymeleaf.org"> 
<head> 
<meta charset="UTF-8"> 
<title>Title</title> 
</head> 
<body> 
    动态500错误页面 
    <p th:text="${error}"></p> 
    <p th:text="${status}"></p> 
    <p th:text="${message}"></p> 
</body> 
</html> 
```

这时访问 [http://localhost:8080/spring/ex](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A8080%2Fspring%2Fex)  即可看到如下错误，说明确实映射到了500.html

![img](https:////upload-images.jianshu.io/upload_images/5811881-c783628a3f7f62e3.png?imageMogr2/auto-orient/strip|imageView2/2/w/602/format/webp)

image.png

> 注:如果同时存在静态页面500.html和动态模板的500.html，则后者覆盖前者。即`templates/error/`这个的优先级比`resources/public/error`高。

整体概括上面几种情况，如下：

- error.html会覆盖默认的 whitelabel  Error Page 错误提示
- 静态错误页面优先级别比error.html高
- 动态模板错误页面优先级比静态错误页面高

3、上面介绍的只是最简单的覆盖错误页面的方式来自定义，如果对于某些错误你可能想特殊对待，则可以这样



```java
@Configuration 
public class ContainerConfig { 
    @Bean 
    public EmbeddedServletContainerCustomizer containerCustomizer(){ 
        return new EmbeddedServletContainerCustomizer(){ 
           @Override 
           public void customize(ConfigurableEmbeddedServletContainer container) { 
               container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500")); 
           } 
        }; 
   } 
} 
```

上面这段代码中`HttpStatus.INTERNAL_SERVER_ERROR`就是对应500错误码，也就是说程序如果发生500错误，就会将请求转发到`/error/500`这个映射来，那我们只要实现一个方法是对应这个`/error/500`映射即可捕获这个异常做出处理



```kotlin
@RequestMapping("/error/500") 
@ResponseBody 
public String showServerError() { 
    return "server error"; 
} 
```

这样，我们再请求前面提到的异常请求 [http://localhost:8080/spring/ex](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A8080%2Fspring%2Fex) 的时候，就会被我们这个方法捕获了。

![img](https:////upload-images.jianshu.io/upload_images/5811881-74d05547e65dd548.png?imageMogr2/auto-orient/strip|imageView2/2/w/596/format/webp)

image.png



这里我们就只对500做了特殊处理，并且返还的是字符串，如果想要返回视图，去掉 @ResponseBody注解，并返回对应的视图页面。如果想要对其他状态码自定义映射，在customize方法中添加即可。

上面这种方法虽然我们重写了/500映射，但是有一个问题就是无法获取错误信息，想获取错误信息的话，我们可以继承BasicErrorController或者干脆自己实现ErrorController接口，除了用来响应/error这个错误页面请求，可以提供更多类型的错误格式等（BasicErrorController在上面介绍SpringBoot默认异常机制的时候有提到）

这里博主选择直接继承BasicErrorController，然后把上面 `/error/500`映射方法添加进来即可



```dart
@Controller
public class MyBasicErrorController extends BasicErrorController {

    public MyBasicErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
    * 定义500的ModelAndView
    * @param request
    * @param response
    * @return
    */

    @RequestMapping(produces = "text/html",value = "/500")
    public ModelAndView errorHtml500(HttpServletRequest request,HttpServletResponse response) {
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request,isIncludeStackTrace(request, MediaType.TEXT_HTML));
        model.put("msg","自定义错误信息");
        return new ModelAndView("error/500", model);
    }

    /**
    * 定义500的错误JSON信息
    * @param request
    * @return
    */

    @RequestMapping(value = "/500")
    @ResponseBody

    public ResponseEntity<Map<String, Object>> error500(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,isIncludeStackTrace(request, MediaType.TEXT_HTML));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<Map<String, Object>>(body, status);
    }
}
```

代码也很简单，只是实现了自定义的500错误的映射解析，分别对浏览器请求以及json请求做了回应。

BasicErrorController默认对应的@RequestMapping是`/error`，固我们方法里面对应的`@RequestMapping(produces = "text/html",value = "/500")`实际上完整的映射请求是`/error/500`，这就跟上面 customize 方法自定义的映射路径对上了。

errorHtml500 方法中，我返回的是模板页面，对应/templates/error/500.html，这里顺便自定义了一个msg信息，在500.html也输出这个信息`<p th:text="${msg}"></p>`，如果输出结果有这个信息，则表示我们配置正确了。

再次访问请求http:[//localhost:8080/spring/ex](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A8080%2Fspring%2Fex%E8%AF%B7%E6%B1%82%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E5%B0%B1%E4%BC%9A%E8%A2%AB%E6%88%91%E4%BB%AC%E8%BF%99%E4%B8%AA%E6%96%B9%E6%B3%95%E6%8D%95%E8%8E%B7%E4%BA%86%E3%80%82) ，结果如下

![img](https:////upload-images.jianshu.io/upload_images/5811881-1932bdd3320fcc31.png?imageMogr2/auto-orient/strip|imageView2/2/w/580/format/webp)

image.png



## 通过@ControllerAdvice注解来处理异常

Spring Boot提供的ErrorController是一种全局性的容错机制。此外，你还可以用@ControllerAdvice注解和@ExceptionHandler注解实现对指定异常的特殊处理。

这里介绍两种情况：

- 局部异常处理  @Controller + @ExceptionHandler
- 全局异常处理  @ControllerAdvice + @ExceptionHandler

### **局部异常处理 @Controller + @ExceptionHandler**

局部异常主要用到的是@ExceptionHandler注解，此注解注解到类的方法上，当此注解里定义的异常抛出时，此方法会被执行。如果@ExceptionHandler所在的类是@Controller，则此方法只作用在此类。如果@ExceptionHandler所在的类带有@ControllerAdvice注解，则此方法会作用在全局。

该注解用于标注处理方法处理那些特定的异常。被该注解标注的方法可以有以下任意顺序的参数类型：

- Throwable、Exception 等异常对象；
- ServletRequest、HttpServletRequest、ServletResponse、HttpServletResponse；
- HttpSession 等会话对象；
- org.springframework.web.context.request.WebRequest；
- java.util.Locale；
- java.io.InputStream、java.io.Reader；
- java.io.OutputStream、java.io.Writer；
- org.springframework.ui.Model；

并且被该注解标注的方法可以有以下的返回值类型可选：

- ModelAndView；
- org.springframework.ui.Model；
- java.util.Map；
- org.springframework.web.servlet.View；
- @ResponseBody 注解标注的任意对象；
- HttpEntity<?> or ResponseEntity<?>；
- void；

以上罗列的不完全，更加详细的信息可参考：[Spring ExceptionHandler](https://link.jianshu.com?t=https%3A%2F%2Fdocs.spring.io%2Fspring%2Fdocs%2Fcurrent%2Fjavadoc-api%2Forg%2Fspringframework%2Fweb%2Fbind%2Fannotation%2FExceptionHandler.html)。

举个简单例子，这里我们对除0异常用@ExceptionHandler来捕捉。



```kotlin
@Controller
public class BaseErrorController extends  AbstractController{ 
    private Logger logger = LoggerFactory.getLogger(this.getClass()); 

    @RequestMapping(value="/ex") 
    @ResponseBody 
    public String error(){ 
        int i=5/0; 
        return "ex"; 
  } 

    //局部异常处理 
    @ExceptionHandler(Exception.class) 
    @ResponseBody 
    public String exHandler(Exception e){ 
      // 判断发生异常的类型是除0异常则做出响应 
      if(e instanceof ArithmeticException){ 
          return "发生了除0异常"; 
      } 
      // 未知的异常做出响应 
      return "发生了未知异常"; 
    }
} 
```

![img](https:////upload-images.jianshu.io/upload_images/5811881-151b28650aed0e98.png?imageMogr2/auto-orient/strip|imageView2/2/w/648/format/webp)

image.png

### **全局异常处理  @ControllerAdvice + @ExceptionHandler**

在spring 3.2中，新增了@ControllerAdvice 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中。

**简单的说，进入Controller层的错误才会由@ControllerAdvice处理，拦截器抛出的错误以及访问错误地址的情况@ControllerAdvice处理不了，由SpringBoot默认的异常处理机制处理。**

我们实际开发中，如果是要实现RESTful API，那么默认的JSON错误信息就不是我们想要的，这时候就需要统一一下JSON格式，所以需要封装一下。



```java
/**
* 返回数据
*/
public class AjaxObject extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
 
    public AjaxObject() {
        put("code", 0);
    }
    
    public static AjaxObject error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }
    
    public static AjaxObject error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }
    
    public static AjaxObject error(int code, String msg) {
        AjaxObject r = new AjaxObject();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static AjaxObject ok(String msg) {
        AjaxObject r = new AjaxObject();
        r.put("msg", msg);
        return r;
    }
    
    public static AjaxObject ok(Map<String, Object> map) {
        AjaxObject r = new AjaxObject();
        r.putAll(map);
        return r;
    }
    
    public static AjaxObject ok() {
        return new AjaxObject();
    }

    public AjaxObject put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    
    public AjaxObject data(Object value) {
        super.put("data", value);
        return this;
    }

    public static AjaxObject apiError(String msg) {
        return error(1, msg);
    }
}
```

上面这个AjaxObject就是我平时用的，如果是正确情况返回的就是：



```kotlin
{
    code：0，
    msg：“获取列表成功”，
    data：{ 
        queryList :[]
    }
}
```

正确默认code返回0，data里面可以是集合，也可以是对象，如果是异常情况，返回的json则是：



```undefined
{
    code：500，
    msg：“未知异常，请联系管理员”
}
```

然后创建一个自定义的异常类：



```java
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    private String msg;
    private int code = 500;
    
    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }
    
    public BusinessException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
    
    public BusinessException(int code,String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    
    public BusinessException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
```

注：spring 对于 RuntimeException 异常才会进行事务回滚

Controler中添加一个json映射，用来处理这个异常



```csharp
@Controller
public class BaseErrorController{
    @RequestMapping("/json")
    public void json(ModelMap modelMap) {
        System.out.println(modelMap.get("author"));
        int i=5/0;
    }
}
```

最后创建这个全局异常处理类：



```java
/**
 * 异常处理器
 */
@RestControllerAdvice
public class BusinessExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());



    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("请求有参数才进来");
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "嘟嘟MD");
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e,HttpServletRequest req){
        AjaxObject r = new AjaxObject();
        //业务异常
        if(e instanceof BusinessException){
            r.put("code", ((BusinessException) e).getCode());
            r.put("msg", ((BusinessException) e).getMsg());
        }else{//系统异常
            r.put("code","500");
            r.put("msg","未知异常，请联系管理员");
        }

        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        String contentTypeHeader = req.getHeader("Content-Type");
        String acceptHeader = req.getHeader("Accept");
        String xRequestedWith = req.getHeader("X-Requested-With");
        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            return r;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("msg", e.getMessage());
            modelAndView.addObject("url", req.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.setViewName("error");
            return modelAndView;
        }
    }
}
```

@ExceptionHandler 拦截了异常，我们可以通过该注解实现自定义异常处理。其中，@ExceptionHandler 配置的 value 指定需要拦截的异常类型，上面我配置了拦截Exception，
 再根据不同异常类型返回不同的相应，最后添加判断，如果是Ajax请求，则返回json,如果是非ajax则返回view，这里是返回到error.html页面。

为了展示错误的时候更友好，我封装了下error.html,不仅展示了错误，还添加了跳转百度谷歌以及StackOverFlow的按钮，如下：



```xml
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org" layout:decorator="layout">
<head>
    <title>Spring Boot管理后台</title>
    <script type="text/javascript">
    </script>
</head>
<body>
<div layout:fragment="content" th:remove="tag">
    <div  id="navbar">
        <h1>系统异常统一处理</h1>
        <h3 th:text="'错误信息：'+${msg}"></h3>
        <h3 th:text="'请求地址：'+${url}"></h3>

        <h2>Debug</h2>
        <a th:href="@{'https://www.google.com/webhp?hl=zh-CN#safe=strict&hl=zh-CN&q='+${msg}}"
           class="btn btn-primary btn-lg" target="_blank" id="Google">Google</a>
        <a th:href="@{'https://www.baidu.com/s?wd='+${msg}}" class="btn btn-info btn-lg"  target="_blank" id="Baidu">Baidu</a>
        <a th:href="@{'http://stackoverflow.com/search?q='+${msg}}"
           class="btn btn-default btn-lg"  target="_blank" id="StackOverFlow">StackOverFlow</a>
        <h2>异常堆栈跟踪日志StackTrace</h2>
        <div th:each="line:${stackTrace}">
            <div th:text="${line}"></div>
        </div>
    </div>
</div>
<div layout:fragment="js" th:remove="tag">
</div>
</body>
</html>
```

访问[http://localhost:8080/json](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A8080%2Fjson)的时候,因为是浏览器发起的，返回的是error界面：

![img](https:////upload-images.jianshu.io/upload_images/5811881-da0cad4a189aca3e.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

image.png



如果是ajax请求，返回的就是错误：



```json
{ "msg":"未知异常，请联系管理员", "code":500 }
```

这里我给带@ModelAttribute注解的方法通过Model设置了author值，在json映射方法中通过 ModelMwap 获取到改值。

认真的你可能发现，全局异常类我用的是@RestControllerAdvice，而不是@ControllerAdvice，因为这里返回的主要是json格式，这样可以少写一个@ResponseBody。

# 总结

到此，SpringBoot中对异常的使用也差不多全了，本项目中处理异常的顺序会是这样，当发送一个请求：

- 拦截器那边先判断是否登录，没有则返回登录页。
- 在进入Controller之前，譬如请求一个不存在的地址，返回404错误界面。
- 在执行@RequestMapping时，发现的各种错误（譬如数据库报错、请求参数格式错误/缺失/值非法等）统一由@ControllerAdvice处理，根据是否Ajax返回json或者view。

想要查看更多Spring Boot干货教程,可前往：[Spring Boot干货系列总纲](https://link.jianshu.com?t=http%3A%2F%2Ftengj.top%2F2017%2F04%2F24%2Fspringboot0%2F)

# 源码下载

(￣︶￣)↗[[相关示例完整代码](https://link.jianshu.com?t=https%3A%2F%2Fgithub.com%2Ftengj%2FSpringBootDemo%2Ftree%2Fmaster)]

- chapter13==》Spring Boot干货系列：（十三）Spring Boot全局异常处理整理

一直觉得自己写的不是技术，而是情怀，一篇篇文章是自己这一路走来的痕迹。靠专业技能的成功是最具可复制性的，希望我的这条路能让你少走弯路，希望我能帮你抹去知识的蒙尘，希望我能帮你理清知识的脉络，希望未来技术之巅上有你也有我。



作者：嘟爷MD
链接：https://www.jianshu.com/p/accec85b4039
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。