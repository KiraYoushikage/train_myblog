package com.kirayous.common.exception;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.exception
 * @date 2021/9/9 18:13
 * 自定义异常类
 */
public class MyServeException extends RuntimeException {
    public MyServeException(String message) {
        super(message);
    }

}