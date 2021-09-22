package com.kirayous.api.admin.service;

import com.kirayous.common.dto.UserMenuDTO;
import com.kirayous.common.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kirayous.common.dto.MenuDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KiraYous
 * @since 2021-09-09
 */
public interface IMenuService extends IService<Menu> {

    List<MenuDTO> listMenus();

    List<UserMenuDTO> listUserMenus();
}
