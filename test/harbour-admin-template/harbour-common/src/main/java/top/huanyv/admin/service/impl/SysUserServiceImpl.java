package top.huanyv.admin.service.impl;

import top.huanyv.admin.dao.DeptDao;
import top.huanyv.admin.domain.entity.SysDept;
import top.huanyv.admin.service.SysUserService;
import top.huanyv.admin.dao.UserDao;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.admin.utils.Constants;
import top.huanyv.admin.utils.LoginUtil;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.security.digest.BCryptPasswordDigester;
import top.huanyv.webmvc.security.digest.PasswordDigester;

import java.sql.Ref;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/2/21 19:55
 */
@Bean
public class SysUserServiceImpl implements SysUserService {

    @Inject
    private UserDao userDao;

    @Inject
    private DeptDao deptDao;

    @Inject
    private PasswordDigester passwordDigester;

    @Override
    public Page<SysUser> getPageUser(int pageNum, int pageSize, SysUser user) {
        Page<SysUser> page = userDao.getPageUser(pageNum, pageSize, user);
        for (SysUser sysUser : page.getData()) {
            Long deptId = sysUser.getDeptId();
            if (deptId != null) {
                sysUser.setDept(deptDao.selectById(deptId));
            }
        }
        return page;
    }

    @Override
    public SysUser getUserById(Long id) {
        SysUser sysUser = userDao.selectById(id);
        if (sysUser.getDeptId() != null) {
            SysDept sysDept = deptDao.selectById(sysUser.getDeptId());
            sysUser.setDept(sysDept);
        }
        return sysUser;
    }

    @Override
    public boolean addUser(SysUser user) {
        String loginUsername = LoginUtil.getLoginUsername();
        user.setCreateBy(loginUsername);
        user.setCreateTime(new Date());
        user.setPassword(passwordDigester.digest(Constants.DEFAULT_PASSWORD));
        user.setAvatar(Constants.DEFAULT_HEAD);
        user.setCreateTime(new Date());
        return userDao.insert(user) > 0;
    }

    @Override
    public boolean updateUser(SysUser user) {
        String loginUsername = LoginUtil.getLoginUsername();
        user.setUpdateBy(loginUsername);
        user.setUpdateTime(new Date());
        return userDao.updateUser(user) > 0;
    }

    @Override
    public boolean deleteUserById(String ids) {
        for (String id : ids.split(",")) {
            int i = userDao.deleteById(Long.parseLong(id));
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateRoles(Long userId, Long[] roleIds) {
        // 删除用户对应的所有角色
        userDao.deleteUserRole(userId);
        // 添加所有用户对应的角色
        for (Long roleId : roleIds) {
            int i = userDao.addUserRole(userId, roleId);
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> getPermissionsByUserId(long userId) {
        return userDao.getPermissionsByUserId(userId);
    }


}
