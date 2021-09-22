package com.kirayous.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.kirayous.common.auth.MyUserDetails;
import com.kirayous.common.dto.UserInfoDTO;
import com.kirayous.common.entity.UserAuth;
import com.kirayous.common.entity.UserInfo;
import com.kirayous.common.exception.MyServeException;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.utils
 * @date 2021/9/10 19:03
 *
 * UserAgentUtils的介绍，还是一个非常不错的工具呢
 * user-agent-utils 是一个用来解析 User-Agent 字符串的 Java 类库。
 * 其能够识别的内容包括：
 * 超过150种不同的浏览器；
 * 7种不同的浏览器类型；
 * 超过60种不同的操作系统；
 * 6种不同的设备类型；
 * 9种不同的渲染引擎；
 * 9种不同的Web应用，如HttpClient、Bot。
 */
public class UserUtil {

    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    @SuppressWarnings("all")
    public static UserInfoDTO getLoginUser() {

        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MyUserDetails myUserDetails=null;
        Object principal=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) throw new MyServeException("匿名用户无法操作");
        else if(principal instanceof UserDetails)myUserDetails=(MyUserDetails) principal;

        Assert.notNull(myUserDetails,"无法操作");
//        assert myUserDetails != null;
        return myUserDetails.getUser();
    }

    /**
     * 获取用户信息id
     * @return 用户信息id
     */
    public static Integer getUserInfoId(){
        return getLoginUser().getUserInfoId();
    }

    /**
     * 封装用户登录信息
     *
     * @param user           用户账号
     * @param userInfo       用户信息
     * @param articleLikeSet 点赞文章id集合
     * @param commentLikeSet 点赞评论id集合
     * @param request        请求
     * @return 用户登录信息
     */
    public static MyUserDetails convertLoginUser(UserAuth user, UserInfo userInfo, List<String> roleList, Set<Integer> articleLikeSet, Set<Integer> commentLikeSet, HttpServletRequest request) {
        // 获取登录信息
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 封装权限集合
        return MyUserDetails.builder().user(UserInfoDTO.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userInfo.getEmail())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .ipAddr(ipAddr)
                .ipSource(ipSource)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(new Date())
                .build()).build();

    }

}