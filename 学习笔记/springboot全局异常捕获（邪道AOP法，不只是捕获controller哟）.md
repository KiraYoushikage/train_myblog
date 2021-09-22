







# **1.前言**

任何系统，我们不会傻傻的在每一个地方进行异常捕获和处理，整个系统一般我们会在一个的地方统一进行异常处理，spring boot全局异常处理很简单；

介绍前先说点题外话，我们现在开发系统，都是前后端完全分离的，后端只提供RESTfull API，禁止涉及任何界面，什么thymeleaf、JSP那些后端模板，是绝对禁止使用的，那些东西请扔垃圾箱，不要浪费大好青春去研究，那是堕落；前端则负责界面相关，常用Vue；如果公司还没前后端分离，还在thymeleaf还在前后端一起写，那你还是早做跳槽打算吧，他们养不起你，更养不起你的家人；

前后端分离，后端API，一般对于异常处理，要做得无非两件事，

**1.是记录日志及相应通知处理，这是对内的，**

**2.是给出返回结果给API调用者，这是对外的；**

对API调用者来说，他只需要一个返回结果（包含错误代码、提示信息），其他的他不关心

对后端来说，他只需要记录日志，通知或者给发布相应消息给其他队列处理相关事项；

所以：看到过不少人封装了很多个自定义异常类，其实，完全没有必要，只需要一个异常处理来处理所有异常即可，然后封装一个错误识别码和提示消息的枚举，用于返回给API调用者；然后后端的处理，直接在一个异常处理方法中全部处理就行了，完全没必要封装N多个自定义异常，那没有任何意义；

# **关于异常的思想认识**

我们应该认识到，一切异常，对系统来说，都是不正常的表现，都是属于缺陷，都属于BUG，尽管有些异常是我们主动抛出的；

我们要做的，是应该尽量提高系统可用性，最大限度避免任何异常的出现，而不是去指望完善异常处理来完善系统；

异常处理，是异常无法避免的出现了而采取的一种应急措施，主要目的是对外增加友好性，对内提供补救线索；

不要认为完善的异常处理是系统核心，他不是，不要指望异常处理尽善尽美，不要指望异常处理来给系统缺陷擦屁股；

如果系统异常过多，那么你要做的不是去完善异常处理机制，而是要好好去反思：系统架构设计是否合理，系统逻辑设计是否合理；

# **2.全局异常并处理的方法一（@ControllerAdvice 和 @ExceptionHandler）**

=================================================

在开发中，我们会有如下的场景：某个接口中，存在一些业务异常。例如用户输入的参数校验失败、用户名密码不存在等。当触发这些业务异常时，我们需要抛出这些自定义的业务异常，并对其进行处理。一般我们要把这些异常信息的状态码和异常描述，友好地返回给调用者，调用者则利用状态码等信息判断异常的具体情况。

过去，我们可能需要在 controller 层通过 try/catch 处理。首先 catch 自定义异常，然后 catch 其它异常。对于不同的异常，我们需要在 catch 的同时封装将要返回的对象。然而，这么做的弊端就是代码会变得冗长。每个接口都需要做 try/catch 处理，而且一旦需要调整，所有的接口都需要修改一遍，非常不利于代码的维护，如下段代码所示



```kotlin
@RequestMapping (value = "/test")
public ResponseEntity test() {
    ResponseEntity re = new ResponseEntity();
    // 业务处理
    // ...
    try {
        // 业务
    } catch (BusinessException e) {
        logger.info("业务发生异常，code:" + e.getCode() + "msg:" + e.getMsg());
        re.setCode(e.getCode());
        re.setMsg(e.getMsg());
        return re;
    } catch (Exception e) {
        logger.error("服务错误:", e);
        re.setCode("xxxxx");
        re.setMsg("服务错误");
        return re;
    }
    return re;
}
```

那么，有没有什么方法可以简便地处理这些异常信息呢？答案是肯定的。Spring 3.2 中，新增了 @ControllerAdvice 注解，可以用于定义 @ExceptionHandler 、 @InitBinder 、@ModelAttribute ，并应用到所有 @RequestMapping 中。简单来说就是，可以通过@ControllerAdvice 注解配置一个全局异常处理类，来统一处理 controller 层中的异常，于此同时 controller 中可以不用再写 try/catch，这使得代码既整洁又便于维护。

# **使用方法**

# **定义自定义异常**

有关自定义异常相关知识点这里就不详细说明了，如果不了解的话自行搜索一下。这里贴上一个简单的自定义业务异常类。



```dart
/**
 * 自定义业务异常类
 *
 * @author Yuzhe Ma
 * @date 2018/11/28
 */
@Data
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

注： @Data 为 Lombok 插件。自动生成 set/get 方法。具体使用方法这里就不展开介绍了。

# **@ControllerAdvice + @ExceptionHand` 配置全局异常处理类**



```java
/**
 * 全局异常处理器
 *
 * @author Yuzhe Ma
 * @date 2018/11/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 Exception 异常
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        logger.error("服务错误:", e);
        return new ResponseEntity("xxx", "服务出错");
    }

    /**
     * 处理 BusinessException 异常
     *
     * @param httpServletRequest httpServletRequest
     * @param e                  异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity businessExceptionHandler(HttpServletRequest httpServletRequest, BusinessException e) {
        logger.info("业务异常。code:" + e.getCode() + "msg:" + e.getMsg());
        return new ResponseEntity(e.getCode(), e.getMsg());
    }
}
```

# **@ControllerAdvice**

定义该类为全局异常处理类。

# **@ExceptionHandler**

定义该方法为异常处理方法。value 的值为需要处理的异常类的 class 文件。在例子中，方法传入两个参数。一个是对应的 Exception 异常类，一个是 HttpServletRequest 类。当然，除了这两种参数，还支持传入一些其他参数。详见文档 [https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/ExceptionHandler.html](https://links.jianshu.com/go?to=https%3A%2F%2Fdocs.spring.io%2Fspring%2Fdocs%2Fcurrent%2Fjavadoc-api%2Forg%2Fspringframework%2Fweb%2Fbind%2Fannotation%2FExceptionHandler.html)

这样，就可以对不同的异常进行统一处理了。通常，为了使 controller 中不再使用任何 try/catch，也可以在 GlobalExceptionHandler 中对 Exception 做统一处理。这样其他没有用 @ExceptionHandler 配置的异常就都会统一被处理。

# **遇到异常时抛出异常即可**

在业务中，遇到业务异常的地方，直接使用 throw 抛出对应的业务异常即可。例如



```cpp
throw new BusinessException("3000", "账户密码错误");
```

# **在 Controller 中的写法**

Controller 中，不需要再写 try/catch，除非特殊用途。



```csharp
@RequestMapping(value = "/test")
public ResponseEntity test() {
    ResponseEntity re = new ResponseEntity();
    // 业务处理
    // ...
    return re;
}
```

# **结果展示**

异常抛出后，返回如下结果。



```json
{
    "code": "3000",
    "msg": "账户密码错误",
    "data": null
}
```

# **注意**

1. 不一定必须在 controller 层本身抛出异常才能被 GlobalExceptionHandler 处理，只要异常最后是从 contoller 层抛出去的就可以被全局异常处理器处理。
2. 异步方法中的异常不会被全局异常处理。
3. 抛出的异常如果被代码内的 try/catch 捕获了，就不会被 GlobalExceptionHandler 处理了。

# **总结**

本文介绍了在 SpringBoot 中，通过配置全局异常处理器统一处理 Controller 层引发的异常。

# **优点**

减少代码冗余，代码便于维护

# **缺点**

只能处理 controller 层抛出的异常，对例如 Interceptor（拦截器）层的异常、定时任务中的异常、异步方法中的异常，不会进行处理。

以上就是用 @ControllerAdvice + @ExceptionHand 实现 SpringBoot 中捕获 controller 层全局异常并处理的方法。

# **3.全局异常并处理的方法二 (AOP)*(重点)*

虽然@ControllerAdvice注解通常和@ExceptionHandler注解用于全局异常的处理。

但是这种方式有个缺点就是，只是对控制层进行了异常拦截，比如像工具类中或者其他类中的异常，并不会拦截。

由于业务执行时不能保证程序不出错,所以写代码必须添加try-catch,但是如果频繁的添加try-catch则必然导致代码结构混乱.所以需要进行优化.

原则:如果出现了问题一般将检查异常,转化为运行时异常.

核心原理: 代理动态思想------->AOP操作

采用自定义AOP的方式可以实现拦截。

有几个关键点

1. 定义切入点为最大项目包
2. 采用AOP的@AfterThrowing注解获取到全局异常捕获一个例子package com.example.promethuesdemo.exception; import lombok.extern.slf4j.Slf4j; import org.aspectj.lang.JoinPoint; import org.aspectj.lang.annotation.AfterThrowing; import org.aspectj.lang.annotation.Aspect; import org.aspectj.lang.annotation.Pointcut; import org.springframework.stereotype.Component; /** * @author chenzhen * Created by chenzhen on 2020/7/20.



```java
*/
    @Aspect
    @Slf4j
    @Component
    public class GlobalExceptionAspect {
        @Pointcut("execution(* com.example..*.*(..))")
        public void pointcut(){

        }

        @AfterThrowing(pointcut = "pointcut()",throwing = "e")
        public void afterThrowing(JoinPoint joinPoint,Throwable e){
            log.error("全局捕获到异常了..............");
            //纪录错误信息
            log.error("系统错误:{}", e.getMessage());
            // todo 想要执行的操作
        }

    }
```

# **aop中相关概念**

- Aspect（切面）： Aspect 声明类似于 Java 中的类声明，在 Aspect 中会包含着一些 Pointcut 以及相应的 Advice。*   Joint point（连接点）：表示在程序中明确定义的点，典型的包括方法调用，对类成员的访问以及异常处理程序块的执行等等，它自身还可以嵌套其它
   joint point。*   Pointcut（切点）：表示一组 joint point，这些 joint point 或是通过逻辑关系组合起来，或是通过通配、正则表达式等方式集中起来，它定义了相应的 Advice 将要发生的地方。*   Advice（增强）：Advice 定义了在 Pointcut 里面定义的程序点具体要做的操作，它通过 before、after 和 around 来区别是在每个 joint point 之前、之后还是代替执行的代码。*   Target（目标对象）：织入 Advice 的目标对象.。 Weaving（织入）：将 Aspect 和其他对象连接起来, 并创建 Adviced object 的过程

# **Advice（增强）的类型**

- before advice, 在 join point 前被执行的 advice. 虽然 before advice 是在 join point 前被执行, 但是它并不能够阻止 join point 的执行, 除非发生了异常(即我们在 before advice 代码中,
   不能人为地决定是否继续执行 join point 中的代码)*   after return advice, 在一个 join point 正常返回后执行的 advice*   after throwing advice, 当一个 join point 抛出异常后执行的 advice*   after(final) advice, 无论一个 join point 是正常退出还是发生了异常, 都会被执行的 advice.*   around advice, 在 join point 前和 joint point 退出后都执行的 advice. 这个是最常用的 advice.*   introduction，introduction可以为原有的对象增加新的属性和方法。

# **注意**

spring AOP中的AfterThrowing增强处理可以对目标方法的异常进行处理，但这种处理与直接使用catch捕捉处理异常的方式不同，catch捕捉意味着能完全处理异常，即只要catch块本身不抛出新的异常，则被处理的异常不会往上级调用者进一步传播下去；但是如果使用了AfterThrowing增强处理用于对异常进行处理，处理后异常仍然会往上一级调用者传播，如果是在main中调用的目标方法，那么异常会直接传到JVM，如下截图所示：

![img](https:////upload-images.jianshu.io/upload_images/20317748-c6b1ce4b0b724b0e?imageMogr2/auto-orient/strip|imageView2/2/w/550/format/webp)

SpringBoot 之配置全局异常处理器捕获异常

另外需要注意， 如果目标方法中出现异常，并由catch捕捉处理且catch又没有抛出新的异常，那么针对该目标方法的AfterThrowing增强处理将不会被执行。

来源：[https://www.tuicool.com/articles/3umAFjJ](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.tuicool.com%2Farticles%2F3umAFjJ)



作者：Java旺
链接：https://www.jianshu.com/p/ed89e61a3b79
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。