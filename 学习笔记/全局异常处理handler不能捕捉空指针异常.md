# 遇到某行代码抛出空指针问题，但全局异常捕获不到





首先你可能需要补充补充java异常的基础只是，请查看“java异常类基础知识.md”

以及需要补充springboot的全局异常处理器"springboot全局异常处理器的使用方法.md"





# 1.问题描述

>某代码行抛出空指针异常
>
>```java
>@Data
>@Builder
>public class MyUserDetails implements UserDetails {
>
>
>
>    private UserInfoDTO user;
>
>    @Override
>    public Collection<? extends GrantedAuthority> getAuthorities() {
>//        System.out.println(this.getUser().getRoleList().toString());
>        //getRoleList()获取到的对象为null值，所以报空指针异常
>        
>        
>        return this.user.getRoleList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
>    }
>	//以下省略许多代码.....
>}
>
>
>```
>
>结果：
>
>![image-20210911161441193](E:\项目\train\myblog\学习笔记\assets\全局异常处理handler不能捕捉空指针异常.assets文件夹\image-20210911161441193.png)

# 2.疑惑点

>上述已经抛出异常，当然浏览器收到由springboot默认处理器处理得到的结果->500状态码
>
>![image-20210911161549280](E:\项目\train\myblog\学习笔记\assets\全局异常处理handler不能捕捉空指针异常.assets文件夹\image-20210911161549280.png)
>
>response:
>
>![image-20210911161624959](E:\项目\train\myblog\学习笔记\assets\全局异常处理handler不能捕捉空指针异常.assets文件夹\image-20210911161624959.png)



>
>
>但是问题是我已经在springboot里面定义了全局异常处理器，并且还是高级别的Exception类，而java.lang.NullPointerException 按理来说是Exception的子类，全局异常处理器完全有理由捕获到

以下为全局处理器代码：

>```java
>/**
> * @author KiraYous
> * @version V1.0
> * @Package com.kirayous.handler
> * @date 2021/7/16 3:25
> */
>
>@ControllerAdvice//增强型controller注解，使用最多的是配合全局异常处理，当异常抛至controller层的时候会由此handler处理
>public class MyGlobalExceptionHandler {
>
>
>//    @ResponseBody
>//    @ExceptionHandler(value = Exception.class)
>//    public Result errorHandler2(RuntimeException ex) {
>//        //判断异常的类型,返回不一样的返回值
>////        if(ex instanceof MissingServletRequestParameterException){
>////            map.put("msg","缺少必需参数："+((MissingServletRequestParameterException) ex).getParameterName());
>////        }
>////        else if(ex instanceof MyException){
>////            map.put("msg","这是自定义异常");
>////        }
>//        return new Result().setCode(400).setMessage(ex.getMessage());
>//    }
>
>    @ResponseBody
>    @ExceptionHandler(value = Exception.class)
>    public Result errorHandler(Exception ex) {
>        //判断异常的类型,返回不一样的返回值
>//        if(ex instanceof MissingServletRequestParameterException){
>//            map.put("msg","缺少必需参数："+((MissingServletRequestParameterException) ex).getParameterName());
>//        }
>//        else if(ex instanceof MyException){
>//            map.put("msg","这是自定义异常");
>//        }
>
>        System.out.println("捕获异常");
>        return new Result().setCode(ResultInfo.ERROR.getCode()).setMessage(ex.getMessage());
>    }
>
>
>}
>```
>
>





# 3.原因

>
>
>首先得骂自己一顿，空指针异常这种低级错误还能犯，还不加以检查，
>
>而且还忘记@ControllerAdvice修饰的异常处理器只能捕获到抛至controller层的异常，而其他@Service或者是其他框架内部的类，是无法进行捕获的



# 4.解决方法

>
>
>
