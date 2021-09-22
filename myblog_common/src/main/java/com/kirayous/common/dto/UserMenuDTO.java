package com.kirayous.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.dto
 * @date 2021/9/9 20:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * icon
     */
    private String icon;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private Integer hidden;

    /**
     * 子菜单列表
     */
    @ApiModelProperty(value = "子菜单列表")
    private List<UserMenuDTO> children;

}
