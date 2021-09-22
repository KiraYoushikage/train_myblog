package com.kirayous.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.dto
 * @date 2021/9/19 17:30
 *
 * 用户角色DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;

}