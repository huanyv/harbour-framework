package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.RoleDao;
import top.huanyv.admin.domain.entity.SysRole;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.admin.service.SysRoleService;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/3/15 20:26
 */
@Component
public class SysRoleServiceImpl implements SysRoleService {

    @Inject
    private RoleDao roleDao;

    @Override
    public Page<SysRole> getPage(int pageNum, int pageSize, SysRole role) {
        return roleDao.getPage(pageNum, pageSize, role);
    }

    @Override
    public List<SysRole> list() {
        return roleDao.selectAll();
    }

    @Override
    public List<SysRole> listRoleByUserId(Long userId) {
        return roleDao.listRoleByUserId(userId);
    }

    @Override
    public boolean add(SysRole role) {
        String loginUsername = LoginUtil.getLoginUsername();
        role.setCreateBy(loginUsername);
        role.setCreateTime(new Date());
        return roleDao.insert(role) > 0;
    }

    @Override
    public boolean updateRoleById(SysRole role) {
        String loginUsername = LoginUtil.getLoginUsername();
        role.setUpdateBy(loginUsername);
        role.setUpdateTime(new Date());
        return roleDao.updateById(role) > 0;
    }

    @Override
    public boolean deleteRoleById(String ids) {
        for (String s : ids.split(",")) {
            int i = roleDao.deleteById(Long.parseLong(s));
            if(i <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleDao.getMenuIdsByRoleId(roleId);
    }

    @Override
    public boolean updateRoleMenu(Long roleId, Long[] menuIds) {
        roleDao.deleteRoleMenuByRoleId(roleId);
        for (Long menuId : menuIds) {
            int i = roleDao.updateRoleMenu(roleId, menuId);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }
}
