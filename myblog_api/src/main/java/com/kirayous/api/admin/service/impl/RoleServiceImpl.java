package com.kirayous.api.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kirayous.common.dto.UserRoleDTO;
import com.kirayous.common.entity.Role;
import com.kirayous.api.admin.mapper.RoleMapper;
import com.kirayous.api.admin.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<String> listRolesByUserAuthId(Integer id) {

        return roleMapper.listRolesByUserAuthId(id);
    }

    @Override
    public List<Role> listUserRoles() {
        // 查询角色列表
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
//        return BeanCopyUtil.copyList(roleList, UserRoleDTO.class);
        return roleList;
    }
}
