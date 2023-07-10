package top.huanyv.admin.dao;

import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/2/21 19:50
 */
public interface UserDao extends BaseDao<SysUser> {

    Page<SysUser> getPageUser(int pageNum, int pageSize, SysUser user);

    SysUser getUserByUsername(String username);

    int addUser(SysUser user);

    int updateUser(SysUser user);

    int deleteUserById(Long id);

    int deleteUserRole(Long userId);

    int addUserRole(Long userId, Long roleId);

    /**
     * 根据用户ID获取对应的权限列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getPermissionsByUserId(long userId);
}
