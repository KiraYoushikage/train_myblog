package com.kirayous.api.admin.service;

import com.kirayous.common.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kirayous.common.vo.UserInfoVO;
import com.kirayous.common.vo.UserRoleVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
public interface IUserInfoService extends IService<UserInfo> {


    /**
     * 修改用户头像
     *
     * @param file 头像图片
     * @return 头像OSS地址
     */
    String updateUserAvatar(MultipartFile file);



    /**
     * 修改用户资料
     *
     * @param userInfoVO 用户资料
     */
    void updateUserInfo(UserInfoVO userInfoVO);


    /**
     * 修改用户禁用状态
     *
     * @param userInfoId 用户信息id
     * @param isDisable  禁用状态
     */
    void updateUserDisable(Integer userInfoId, Integer isDisable);


    /**
     * 修改用户权限
     *
     * @param userRoleVO 用户权限
     */
    void updateUserRole(UserRoleVO userRoleVO);
}
