package top.huanyv.admin.service;

import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/3/15 20:26
 */
public interface SysRoleService {
    Page<SysRole> getPage(int pageNum, int pageSize, SysRole role);

    List<SysRole> list();

    List<SysRole> listRoleByUserId(Long userId);

    boolean add(SysRole role);

    boolean updateRoleById(SysRole role);

    boolean deleteRoleById(String ids);

    List<Long> getMenuIdsByRoleId(Long roleId);

    boolean updateRoleMenu(Long roleId, Long[] menuIds);
}
