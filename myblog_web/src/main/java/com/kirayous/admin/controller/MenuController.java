package com.kirayous.admin.controller;

import com.kirayous.api.admin.service.IMenuService;
import com.kirayous.common.Result;
import com.kirayous.common.dto.MenuDTO;
import com.kirayous.common.dto.UserMenuDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.admin.controller
 * @date 2021/9/9 18:32
 */
@RestController
@Api(tags = "菜单展示模块")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public Result listMenus() {
        List<MenuDTO> list=menuService.listMenus();
        return Result.success().setData(list);
    }

//    @ApiOperation(value = "查看用户菜单")
//    @GetMapping("/admin/user/menus")
//    public Result listUserMenus() {
//        List<MenuDTO> list=menuService.listMenus();
//        return Result.success().setData(list);
//
//    }

    @ApiOperation(value = "查看用户菜单")
    @GetMapping("/admin/user/menus")
    public Result listUserMenus() {
        List <UserMenuDTO> list=menuService.listUserMenus();
        return Result.success().setData(list);
    }
}
