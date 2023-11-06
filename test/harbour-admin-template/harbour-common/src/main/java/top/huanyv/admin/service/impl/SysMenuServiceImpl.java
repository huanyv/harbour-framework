package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.MenuDao;
import top.huanyv.admin.domain.entity.SysMenu;
import top.huanyv.admin.service.SysMenuService;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;

import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/22 14:23
 */
@Bean
public class SysMenuServiceImpl implements SysMenuService {

    @Inject
    private MenuDao menuDao;

    @Override
    public List<SysMenu> getMenus() {
        return menuDao.getMenus();
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return menuDao.getMenusByUserId(userId);
    }

    @Override
    public boolean updateMenuById(SysMenu menu) {
        String loginUsername = LoginUtil.getLoginUsername();
        menu.setUpdateBy(loginUsername);
        menu.setUpdateTime(new Date());
        return menuDao.updateById(menu) > 0;
    }

    @Override
    public boolean addMenu(SysMenu menu) {
        String loginUsername = LoginUtil.getLoginUsername();
        menu.setCreateBy(loginUsername);
        menu.setCreateTime(new Date());
        menu.setMenuId(null);
        return menuDao.insert(menu) > 0;
    }

    @Override
    public boolean deleteMenuById(Long id) {
        // TODO 删除菜单对应角色关系
        return menuDao.deleteById(id) > 0;
    }
}
