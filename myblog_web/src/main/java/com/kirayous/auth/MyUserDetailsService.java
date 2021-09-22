package com.kirayous.auth;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kirayous.api.admin.service.IRoleService;
import com.kirayous.api.admin.service.IUserAuthService;
import com.kirayous.api.admin.service.IUserInfoService;
import com.kirayous.api.service.MyRedisService;
import com.kirayous.common.constant.RedisPrefixConst;
import com.kirayous.common.entity.UserAuth;
import com.kirayous.common.entity.UserInfo;

import com.kirayous.common.utils.UserUtil;

import com.kirayous.common.exception.MyServeException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.auth
 * @date 2021/8/23 15:04
 */
@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    IUserAuthService userAuthService;
    @Autowired
    IUserInfoService userInfoService;
    @Autowired
    IRoleService roleService;
    @Autowired
    MyRedisService redisService;

//    @Autowired
//    BCryptPasswordEncoder encoder;
    @Resource
    private HttpServletRequest request;
    @Override
    public UserDetails loadUserByUsername(String username) {
        if(StringUtils.isBlank(username)){
//            throw new UsernameNotFoundException("用户名为空");
            throw new MyServeException("用户名不能为空！");
        }

        // 查询账号是否存在
        UserAuth user = userAuthService.getBaseMapper().selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, username));
        if (Objects.isNull(user)) {
            throw new MyServeException("用户名不存在!");
//            throw new UsernameNotFoundException("用户名不存在！");
        }
        System.out.println(user);

        // 查询账号信息
        UserInfo userInfo = userInfoService.getBaseMapper().selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, user.getUserInfoId()));


        // 查询账号角色
        List<String> roleList = roleService.listRolesByUserAuthId(user.getId());
        // 查询账号点赞信息
        Set<Integer> articleLikeSet = (Set<Integer>) redisService.hGet(RedisPrefixConst.ARTICLE_USER_LIKE, userInfo.getId().toString());
        Set<Integer> commentLikeSet = (Set<Integer>) redisService.hGet(RedisPrefixConst.COMMENT_USER_LIKE, userInfo.getId().toString());
        // 封装登录信息
        return UserUtil.convertLoginUser(user, userInfo, roleList, articleLikeSet, commentLikeSet, request);
    }
}
