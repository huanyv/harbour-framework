package top.huanyv.admin.service;

import top.huanyv.admin.domain.entity.SysMenu;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/22 14:22
 */
public interface SysMenuService {

    List<SysMenu> getMenus();

    List<SysMenu> getMenusByUserId(Long userId);

    boolean updateMenuById(SysMenu menu);

    boolean addMenu(SysMenu menu);

    boolean deleteMenuById(Long id);
}
