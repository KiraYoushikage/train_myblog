package com.kirayous.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common
 * @date 2021/7/16 3:00
 */
@Data
@Accessors(chain = true)
public class Result implements Serializable {

    /*
    * 返回数据的状态 status ->false or true
    * */
    @ApiModelProperty(value = "是否操作成功")
    private boolean status;
    /*
    * 返回状态码 code
    * */
    @ApiModelProperty(value = "状态码")
    private Integer code;
    /*
    * 返回信息 message
    * */
    @ApiModelProperty(value = "返回信息")
    private String message;

    /*
    * 返回数据 data
    * */
    @ApiModelProperty(value = "返回数据")
    private Object data;

    public static  Result success()
    {
        Result result=new Result();
        result.setStatus(true);
        result.setCode(ResultInfo.SUCCESS.getCode());
        result.setMessage(ResultInfo.SUCCESS.getMessage());
        return  result;
    }
    public  static Result success(Integer code,String message,Object data)
    {
        return  new Result().setStatus(true).setCode(code).setData(data).setMessage(message);
    }

    public static  Result error()
    {
        Result result=new Result();
        result.setStatus(false);
        result.setCode(ResultInfo.ERROR.getCode());
        result.setMessage(ResultInfo.ERROR.getMessage());
        return  result;
    }
    public static  Result error(Integer code,String message,Object data)
    {
        return new Result().setStatus(false).setCode(code).setData(data).setMessage(message);
    }

}
