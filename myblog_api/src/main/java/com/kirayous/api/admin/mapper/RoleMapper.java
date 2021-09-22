package com.kirayous.api.admin.mapper;

import com.kirayous.common.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-10
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {


    @Select("        SELECT\n" +
            "         role_label\n" +
            "        FROM\n" +
            "         tb_role r,\n" +
            "         tb_user_role ur\n" +
            "         WHERE\n" +
            "         r.id = ur.role_id\n" +
            "        AND ur.user_id = #{userInfoId}")
    List<String> listRolesByUserAuthId(@Param("userInfoId") Integer id);
}
