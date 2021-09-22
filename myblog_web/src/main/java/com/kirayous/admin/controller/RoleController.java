package com.kirayous.admin.controller;


import com.kirayous.api.admin.service.IRoleService;
import com.kirayous.common.Result;
import com.kirayous.common.dto.UserRoleDTO;
import com.kirayous.common.entity.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-21
 */

@Api(tags = "角色模块")
@RestController
public class RoleController {
    @Autowired
    IRoleService roleService;


    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result listUserRole() {
        List <Role> role=roleService.listUserRoles();
       return Result.success().setMessage("查询成功").setData(role);
    }
}

