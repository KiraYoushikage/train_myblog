package com.kirayous.common.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.enums
 * @date 2021/9/16 21:14
 */
@Getter
public enum PicExtEnum {


    //到时候最好是弄一个正则表达式来匹配比较好
    JPG("jpg", "jpg格式"),
    PNG("png", "png格式");

    private String ext;
    private String desc;
    PicExtEnum(String ext, String desc) {
        this.ext=ext;
        this.desc=desc;
    }


    public static List<String> getAllPicExt() {
        return Arrays.stream(PicExtEnum.values()).map(picExtEnum -> picExtEnum.getExt()).collect(Collectors.toList());
    }
}
