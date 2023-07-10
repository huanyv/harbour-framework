package top.huanyv.admin.dao.impl;

import top.huanyv.admin.dao.MenuDao;
import top.huanyv.admin.domain.entity.SysMenu;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/22 14:21
 */
@Dao
public class MenuDaoImpl implements MenuDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public List<SysMenu> getMenus() {
        String sql = "select * from sys_menu";
        return sqlContext.selectList(SysMenu.class, sql);
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        String sql = "select * from sys_user_role t1 " +
                "join sys_role_menu t2 on t1.role_id = t2.role_id " +
                "join sys_menu t3 on t3.menu_id = t2.menu_id " +
                "where t1.user_id = ? and t3.menu_type in ('M', 'C') and t3.status = '0'";
        return sqlContext.selectList(SysMenu.class, sql, userId);
    }
}
