package top.huanyv.admin.dao;

import top.huanyv.admin.domain.entity.SysMenu;
import top.huanyv.jdbc.builder.BaseDao;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/22 14:21
 */
public interface MenuDao extends BaseDao<SysMenu> {

   List<SysMenu> getMenus();

   List<SysMenu> getMenusByUserId(Long userId);

}
