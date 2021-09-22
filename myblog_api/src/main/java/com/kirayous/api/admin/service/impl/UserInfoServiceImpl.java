package com.kirayous.api.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kirayous.api.admin.mapper.UserRoleMapper;
import com.kirayous.api.admin.service.IUserRoleService;
import com.kirayous.common.entity.UserInfo;
import com.kirayous.api.admin.mapper.UserInfoMapper;
import com.kirayous.api.admin.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirayous.common.entity.UserRole;
import com.kirayous.common.enums.FilePathEnum;
import com.kirayous.common.utils.OSSUtil;
import com.kirayous.common.utils.UserUtil;
import com.kirayous.common.vo.UserInfoVO;
import com.kirayous.common.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {


    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    IUserRoleService userRoleService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {

        // 头像上传oss，返回图片地址
        String avatar = OSSUtil.upload(file, FilePathEnum.AVATAR.getPath());
        if (Objects.isNull(avatar))return null;
        // 更新用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .avatar(avatar)
                .build();
        int res=this.baseMapper.updateById(userInfo);
        return res!=-1?avatar:null;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite())
                .updateTime(LocalDateTime.now())
                .build();
        this.baseMapper.updateById(userInfo);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(Integer userInfoId, Integer isDisable) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userInfoId)
                .isDisable(isDisable)
                .build();
        this.baseMapper.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname())
                .build();
        this.baseMapper.updateById(userInfo);
        // 删除关联表tb_user_role中用户角色，然后重新添加
        // TODO待优化
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        for(Integer roleId:userRoleVO.getRoleIdList()){
            userRoleMapper.insert(UserRole.builder()
                    .userId(userRoleVO.getUserInfoId())
                    .roleId(roleId).build());
        }
//        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
//                .map(roleId -> UserRole.builder()
//                        .roleId(roleId)
//                        .userId(userRoleVO.getUserInfoId())
//                        .build())
//                .collect(Collectors.toList());
        //userRoleMapper.insert(userRoleList);
//        userRoleService.saveBatch();
//        userRoleService.saveBatch();

        //花里胡哨的，没必要这么写
//        userRoleVO.getRoleIdList().stream().peek(roleId ->{
//            userRoleMapper.insert(UserRole.builder()
//                    .userId(userRoleVO.getUserInfoId())
//                    .roleId(roleId).build());
//        } ).forEach(System.out::println);

    }
}
