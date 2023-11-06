package top.huanyv.admin.dao.impl;

import ch.qos.logback.classic.db.SQLBuilder;
import top.huanyv.admin.dao.UserDao;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.jdbc.annotation.*;
import top.huanyv.jdbc.builder.SqlBuilder;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.bean.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/2/21 19:50
 */
@Dao
public class UserDaoImpl implements UserDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<SysUser> getPageUser(int pageNum, int pageSize, SysUser user) {
        SqlBuilder sb = new SqlBuilder("select * from sys_user")
                .condition("where", c -> c
                        .append(StringUtil.hasText(user.getUserName()), "user_name like concat('%', ?, '%')", user.getUserName())
                        .and(StringUtil.hasText(user.getNickName()), "nick_name like concat('%', ?, '%')", user.getNickName())
                        .and(StringUtil.hasText(user.getEmail()), "email like concat('%', ?, '%')", user.getEmail())
                        .and(StringUtil.hasText(user.getPhonenumber()), "phonenumber like concat('%', ?, '%')", user.getPhonenumber())
                        .and(StringUtil.hasText(user.getSex()), "sex = ?", user.getSex())
                );
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, SysUser.class, sb.getSql(), sb.getArgs());
        return page;
    }

    @Override
    @Select("select * from sys_user where user_name = ? and status = 0")
    public SysUser getUserByUsername(String username) {
        return null;
    }

    @Override
    @Insert("insert into sys_user(user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, login_ip, login_date, create_by, create_time, update_by, update_time, remark) values(#{userName}, #{nickName}, #{userType}, #{email}, #{phonenumber}, #{sex}, #{avatar}, #{password}, #{status}, #{loginIp}, #{loginDate}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    public int addUser(SysUser user) {
        return 0;
    }

    @Override
    public int updateUser(SysUser user) {
        SqlBuilder sb = new SqlBuilder("update sys_user set")
                .join(", ", join -> join
                        .append(StringUtil.hasText(user.getUserName()), "user_name = #{userName}")
                        .append(Objects.nonNull(user.getDeptId()), "dept_id = #{deptId}")
                        .append(StringUtil.hasText(user.getNickName()), "nick_name = #{nickName}")
                        .append(StringUtil.hasText(user.getUserType()), "user_type = #{userType}")
                        .append(StringUtil.hasText(user.getEmail()), "email = #{email}")
                        .append(StringUtil.hasText(user.getPhonenumber()), "phonenumber = #{phonenumber}")
                        .append(StringUtil.hasText(user.getSex()), "sex = #{sex}")
                        .append(StringUtil.hasText(user.getAvatar()), "avatar = #{avatar}")
                        .append(StringUtil.hasText(user.getPassword()), "password = #{password}")
                        .append(StringUtil.hasText(user.getStatus()), "status = #{status}")
                        .append(StringUtil.hasText(user.getLoginIp()), "login_ip = #{loginIp}")
                        .append(Objects.nonNull(user.getLoginDate()), "login_date = #{loginDate}")
                        .append(StringUtil.hasText(user.getCreateBy()), "create_by = #{createBy}")
                        .append(Objects.nonNull(user.getCreateTime()), "create_time = #{createTime}")
                        .append(StringUtil.hasText(user.getUpdateBy()), "update_by = #{updateBy}")
                        .append(Objects.nonNull(user.getUpdateTime()), "update_time = #{updateTime}")
                        .append(StringUtil.hasText(user.getRemark()), "remark = #{remark}")
                ).append("where user_id = #{userId}");
        return sqlContext.update(sb.getSql(), user);
    }

    @Override
    @Delete("delete from sys_user where user_id = ?")
    public int deleteUserById(Long id) {
        return 0;
    }

    @Override
    @Delete("delete from sys_user_role where user_id = ?")
    public int deleteUserRole(Long userId) {
        return 0;
    }

    @Override
    public int addUserRole(Long userId, Long roleId) {
        String sql = "insert into sys_user_role (user_id, role_id) values(?, ?)";
        return sqlContext.update(sql, userId, roleId);
    }

    @Override
    public List<String> getPermissionsByUserId(long userId) {
        String sql = "select t3.perms from sys_user_role t1\n" +
                "join sys_role_menu t2 on t1.role_id = t2.role_id \n" +
                "join sys_menu t3 on t3.menu_id = t2.menu_id \n" +
                "where t1.user_id = ? and t3.status = '0'";
        List<Map<String, Object>> maps = sqlContext.selectListMap(sql, userId);
        return maps.stream().map(map -> map.get("perms").toString()).collect(Collectors.toList());
    }

}
