package com.kirayous.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kirayous.api.admin.service.IUserInfoService;
import com.kirayous.common.Result;
import com.kirayous.common.annotation.OptLog;
import com.kirayous.common.constant.OptTypeConst;
import com.kirayous.common.vo.UserInfoVO;
import com.kirayous.common.vo.UserRoleVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.admin.controller
 * @date 2021/9/16 20:05
 */
@RestController
public class UserInfoController {

    @Autowired
    IUserInfoService userInfoService;

    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public Result updateUserInfo(MultipartFile file) {
        String avatar =userInfoService.updateUserAvatar(file);

        return !StringUtils.isBlank(avatar) ?Result.success().setMessage("修改成功!").setData(new HashMap<String,String>(){
            {put("avatar",avatar);}
        }):Result.error().setMessage("修改失败");
    }

    @ApiOperation(value = "修改用户资料")
    @PutMapping("/users/info")
    public Result updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return Result.success().setMessage("修改成功");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable/{userInfoId}")
    public Result updateUserSilence(@PathVariable("userInfoId") Integer userInfoId,@RequestParam(value = "isDisable") Integer isDisable) {
        userInfoService.updateUserDisable(userInfoId, isDisable);
        return Result.success().setMessage("修改成功");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    public Result updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return Result.success().setMessage("修改成功");
    }

}
