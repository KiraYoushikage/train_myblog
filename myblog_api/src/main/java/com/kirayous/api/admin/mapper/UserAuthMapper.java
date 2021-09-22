package com.kirayous.api.admin.mapper;

import com.kirayous.common.dto.UserBackDTO;
import com.kirayous.common.entity.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kirayous.common.vo.ConditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {



    /**
     * 查询后台用户数量
     * @param condition 条件
     * @return 用户数量
     *
     * TODO 有待优化，想想这里是否需要join查询？
     */
    Integer countUser(@Param("condition") ConditionVO condition);

    /**
     * 查询后台用户列表
     * @param condition 条件
     * @return 用户集合
     *
     * TODO 这里想想是否是根据UserAuth查询还是UserInfo的id进行查询
     */
    List<UserBackDTO> listUsers(@Param("condition") ConditionVO condition);
}
