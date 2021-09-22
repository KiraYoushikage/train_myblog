package com.kirayous.api.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kirayous.common.dto.PageDTO;
import com.kirayous.common.dto.UserBackDTO;
import com.kirayous.common.entity.UserAuth;
import com.kirayous.api.admin.mapper.UserAuthMapper;
import com.kirayous.api.admin.service.IUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kirayous.common.exception.MyServeException;
import com.kirayous.common.utils.UserUtil;
import com.kirayous.common.vo.ConditionVO;
import com.kirayous.common.vo.PasswordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
@Service
@Primary
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements IUserAuthService {

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserAuthMapper userAuthMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAdminPassword(PasswordVO passwordVO) {
        // 查询旧密码是否正确
        UserAuth user = this.baseMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, UserUtil.getLoginUser().getId()));
        // 正确则修改密码，错误则提示不正确

        if (Objects.nonNull(user) &&passwordEncoder.matches(passwordVO.getOldPassword(), user.getPassword()) /*BCrypt.checkpw(passwordVO.getOldPassword(), user.getPassword())*/) {
            UserAuth userAuth = UserAuth.builder()
                    .id(UserUtil.getLoginUser().getId())
                    //.password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt()))
                    .password(passwordEncoder.encode(passwordVO.getNewPassword()))
                    .build();
            this.baseMapper.updateById(userAuth);
        } else {
            throw new MyServeException("旧密码不正确");
        }
    }



    @Override
    public PageDTO<UserBackDTO> listUserBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 获取后台用户数量

        Integer count = userAuthMapper.countUser(condition);
//        System.out.println(count);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackDTOList = userAuthMapper.listUsers(condition);
        return new PageDTO<>(userBackDTOList, count);
    }
}
