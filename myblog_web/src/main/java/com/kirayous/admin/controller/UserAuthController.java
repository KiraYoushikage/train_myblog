package com.kirayous.admin.controller;

import com.kirayous.api.admin.service.IUserAuthService;
import com.kirayous.common.Result;
import com.kirayous.common.dto.PageDTO;
import com.kirayous.common.dto.UserBackDTO;
import com.kirayous.common.vo.ConditionVO;
import com.kirayous.common.vo.PasswordVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.admin.controller
 * @date 2021/9/18 9:31
 */
@RestController
public class UserAuthController {
    @Autowired
    IUserAuthService userAuthService;

    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public Result updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return Result.success().setMessage("修改成功！");
    }

    @ApiOperation(value = "后台查看用户列表")
    @GetMapping("/admin/users")
    public Result listUsers(ConditionVO condition) {
        PageDTO<UserBackDTO> pageDTO= userAuthService.listUserBackDTO(condition);
        return Result.success().setMessage("查询成功！").setData(pageDTO);
    }
}
