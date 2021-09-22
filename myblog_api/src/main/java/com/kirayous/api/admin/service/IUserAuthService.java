package com.kirayous.api.admin.service;

import com.kirayous.common.dto.PageDTO;
import com.kirayous.common.dto.UserBackDTO;
import com.kirayous.common.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kirayous.common.vo.ConditionVO;
import com.kirayous.common.vo.PasswordVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
public interface IUserAuthService extends IService<UserAuth> {


    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVO passwordVO);


    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageDTO<UserBackDTO> listUserBackDTO(ConditionVO condition);
}
