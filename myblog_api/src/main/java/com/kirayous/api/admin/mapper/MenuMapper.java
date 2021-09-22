package com.kirayous.api.admin.mapper;

import com.kirayous.common.entity.Menu;
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
 * @since 2021-09-09
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


    @Select("     SELECT DISTINCT\n" +
            "            m.id,\n" +
            "            `name`,\n" +
            "            `path`,\n" +
            "            component,\n" +
            "            icon,\n" +
            "            is_hidden,\n" +
            "            parent_id,\n" +
            "            order_num\n" +
            "         FROM\n" +
            "            tb_user_role as ur\n" +
            "            JOIN tb_role_menu as rm ON ur.role_id = rm.role_id\n" +
            "            JOIN `tb_menu` as m ON rm.menu_id = m.id\n" +
            "         WHERE\n" +
            "            user_id = #{userInfoId}")
    List<Menu> listMenusByUserInfoId(@Param("userInfoId") Integer userInfoId);
}
