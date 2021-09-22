package com.kirayous.common;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common
 * @date 2021/9/5 19:45
 */
public enum ResultInfo {
    SUCCESS(200,"操作成功"),
    ERROR(400,"操作失败"),
    NOT_FOUND(404,"没有找到"),
    UPDATE_ERROR(678,"更新失败"),
    UPDATE_SUCCESS(679,"修改成功"),
    NO_DATA_FOUND(999,"没有找到相关内容"),
    LOGIN_SUCCESS(123,"登录成功"),
    LOGIN_FAILED(1232,"用户名或者密码错误"),
    VERIFY_SUCCESS(756,"登录验证成功"),
    VERIFY_FAILED(885,"登录验证失败"),

    /*
    * code
    * */

    /**
     * 成功
     */
     OK(20000,"成功"),

    /**
     * 失败
     */
    ERROR_UNKNOW(20001,"未知错误"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000,"系统异常"),

    /**
     * 未登录
     */
    NOT_LOGIN(40001,"未登录"),

    /**
     * 没有操作权限
     */
    HAVE_NOT_AUTHORIZED(40003,"没有操作权限")
    ;
    private  Integer code;
    private  String message;

    ResultInfo(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
