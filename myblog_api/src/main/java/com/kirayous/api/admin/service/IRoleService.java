package com.kirayous.api.admin.service;

import com.kirayous.common.dto.UserRoleDTO;
import com.kirayous.common.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-10
 */
@SuppressWarnings("all")
public interface IRoleService extends IService<Role> {


    /**
     * 根据用户id获取角色列表
     *
     * @param userAuthId 用户id
     * @return 角色标签
     */
    List<String> listRolesByUserAuthId(Integer id);

    /**
     * 获取所有
     *
     * @return 角色
     */

    List<Role>  listUserRoles();
}
