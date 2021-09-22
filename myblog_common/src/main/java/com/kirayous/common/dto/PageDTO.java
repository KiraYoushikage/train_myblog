package com.kirayous.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.dto
 * @date 2021/9/19 17:32
 * 分页列表，其实后期可以借助mybatis-plus框架提供的分页类实现分页功能
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    /**
     * 分页列表
     */
    private List<T> recordList;

    /**
     * 总数
     */
    private Integer count;

}
