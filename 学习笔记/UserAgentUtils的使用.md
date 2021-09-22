

# UserAgentUtils的使用方法



user-agent-utils 是一个用来解析 User-Agent 字符串的 Java 类库。 其能够识别的内容包括： 超过150种不同的浏览器； 7种不同的浏览器类型； 超过60种不同的操作系统； 6种不同的设备类型； 9种不同的渲染引擎； 9种不同的Web应用，如HttpClient、Bot。

在web应用中我们通过request获取用户的Agent:

```
String agent=request.getHeader("User-Agent");
```

如下，我们获取了一个agent的字符串：

```
"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36"
```

由此，通过User-agent-utils解析：

```
String agent=request.getHeader("User-Agent");
//解析agent字符串
UserAgent userAgent = UserAgent.parseUserAgentString(agent);
//获取浏览器对象
Browser browser = userAgent.getBrowser();
//获取操作系统对象
OperatingSystem operatingSystem = userAgent.getOperatingSystem();

System.out.println("浏览器名:"+browser.getName());
System.out.println("浏览器类型:"+browser.getBrowserType());
System.out.println("浏览器家族:"+browser.getGroup());
System.out.println("浏览器生产厂商:"+browser.getManufacturer());
System.out.println("浏览器使用的渲染引擎:"+browser.getRenderingEngine());
System.out.println("浏览器版本:"+userAgent.getBrowserVersion());
        
System.out.println("操作系统名:"+operatingSystem.getName());
System.out.println("访问设备类型:"+operatingSystem.getDeviceType());
System.out.println("操作系统家族:"+operatingSystem.getGroup());
System.out.println("操作系统生产厂商:"+operatingSystem.getManufacturer());
```

### AOP && UserAgent

使用日志AOP获取请求方法，参数，浏览器信息等

```
@Aspect
@Component
@Slf4j
public class AopLog {
    private static final String START_TIME = "request-start";

    @Pointcut("execution(* com.hjy.log.aop.controller.*.*(..) )")
    public void log(){}

    @Before("log()")
    public void beforeLog(JoinPoint point){
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request= Objects.requireNonNull(attributes).getRequest();

        log.info("【请求 URL】: {}",request.getRequestURL());
        log.info("【请求 IP】: {}",request.getRemoteAddr());
        log.info("【请求类名】: {},【请求方法名】: {}",
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName());

        Map<String,String[]> map= request.getParameterMap();
        log.info("【请求参数】: {}", JSONUtil.toJsonStr(map));
        Long start=System.currentTimeMillis();
        request.setAttribute(START_TIME,start);

    }

    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result=point.proceed();
        log.info("【返回值】：{}",JSONUtil.toJsonStr(result));
        return result;
    }

    @After("log()")
    public void afterLog(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Long start= (Long) request.getAttribute(START_TIME);
        Long end=System.currentTimeMillis();
        log.info("【请求耗时】：{}ms",end-start);

        String header=request.getHeader("User-Agent");
        UserAgent userAgent=UserAgent.parseUserAgentString(header);
        log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}",
                userAgent.getBrowser().toString(),
                userAgent.getOperatingSystem().toString(),
                header);
    }
}
```