package top.huanyv.admin.dao.impl;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_REDPeer;
import top.huanyv.admin.dao.RoleDao;
import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.DefaultSqlContext;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.tools.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/3/15 20:23
 */
@Dao
public class RoleDaoImpl implements RoleDao {

    private final SqlContext sqlContext = new DefaultSqlContext();

    @Override
    public Page<SysRole> getPage(int pageNum, int pageSize, SysRole role) {
        SqlBuilder sb = new SqlBuilder("select * from sys_role")
                .condition("where", c -> c
                    .append(StringUtil.hasText(role.getRoleName()), "role_name like concat('%', ?, '%')", role.getRoleName())
                    .and(StringUtil.hasText(role.getRoleKey()), "role_key like concat('%', ?, '%')", role.getRoleKey())
                ).append("order by role_sort");
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysRole.class, sb.getSql(), sb.getArgs());
        return page;
    }

    @Override
    public List<SysRole> listRoleByUserId(Long userId) {
        String sql = "select * from sys_role t1 join sys_user_role t2 on t1.role_id = t2.role_id where t2.user_id = ?";
        return sqlContext.selectList(SysRole.class, sql, userId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        String sql = "select menu_id from sys_role_menu where role_id = ?";
        List<Map<String, Object>> maps = sqlContext.selectListMap(sql, roleId);
        return maps.stream().map(map -> (Long) map.get("menu_id")).collect(Collectors.toList());
    }

    @Override
    public int deleteRoleMenuByRoleId(Long roleId) {
        String sql = "delete from sys_role_menu where role_id = ?";
        return sqlContext.update(sql, roleId);
    }

    @Override
    public int updateRoleMenu(Long roleId, Long menuId) {
        String sql = "insert into sys_role_menu (role_id, menu_id) values(?, ?)";
        return sqlContext.update(sql, roleId, menuId);
    }

}
