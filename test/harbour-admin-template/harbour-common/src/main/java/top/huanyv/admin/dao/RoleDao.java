package top.huanyv.admin.dao;

import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/3/15 20:22
 */
public interface RoleDao extends BaseDao<SysRole> {

    Page<SysRole> getPage(int pageNum, int pageSize, SysRole role);

    List<SysRole> listRoleByUserId(Long userId);

    List<Long> getMenuIdsByRoleId(Long roleId);

    int deleteRoleMenuByRoleId(Long roleId);

    int updateRoleMenu(Long roleId, Long menuId);
}
