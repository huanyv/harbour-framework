package top.huanyv.admin.service;

import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/21 19:55
 */
public interface SysUserService {

    Page<SysUser> getPageUser(int pageNum, int pageSize, SysUser user);

    SysUser getUserById(Long id);

    boolean addUser(SysUser user);

    boolean updateUser(SysUser user);

    boolean deleteUserById(String ids);

    boolean updateRoles(Long userId, Long[] roleIds);

    /**
     * 根据用户ID，获取权限列表
     *
     * @param userId 用户id
     * @return boolean
     */
    List<String> getPermissionsByUserId(long userId);
}
