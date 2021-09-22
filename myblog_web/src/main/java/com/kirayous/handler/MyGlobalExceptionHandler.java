package com.kirayous.handler;

import com.kirayous.common.Result;
import com.kirayous.common.ResultInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.handler
 * @date 2021/7/16 3:25
 */

//增强型controller注解，使用最多的是配合全局异常处理，当异常抛至controller层的时候会由此handler处理
//@ControllerAdvice(basePackages = {"com.kirayous.admin.controller","com.kirayous.blog.controller"})
public class MyGlobalExceptionHandler {


//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public Result errorHandler2(RuntimeException ex) {
//        //判断异常的类型,返回不一样的返回值
////        if(ex instanceof MissingServletRequestParameterException){
////            map.put("msg","缺少必需参数："+((MissingServletRequestParameterException) ex).getParameterName());
////        }
////        else if(ex instanceof MyException){
////            map.put("msg","这是自定义异常");
////        }
//        return new Result().setCode(400).setMessage(ex.getMessage());
//    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        //判断异常的类型,返回不一样的返回值
//        if(ex instanceof MissingServletRequestParameterException){
//            map.put("msg","缺少必需参数："+((MissingServletRequestParameterException) ex).getParameterName());
//        }
//        else if(ex instanceof MyException){
//            map.put("msg","这是自定义异常");
//        }
        System.out.println("捕获异常");
        return new Result().setCode(ResultInfo.ERROR.getCode()).setMessage(ex.getMessage());
    }


}