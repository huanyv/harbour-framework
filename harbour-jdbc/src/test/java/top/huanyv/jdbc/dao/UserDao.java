package top.huanyv.jdbc.dao;

import top.huanyv.jdbc.annotation.Select;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.jdbc.entity.User;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/1/13 15:52
 */
public interface UserDao extends BaseDao<User> {

    Page<User> pageUser();

    @Select("select * from t_user where id = #{arg0}")
    User getUserById(int id);

    List<User> getUsers();

    int updateUserById(User user);

    int insertUser(User user);

    int deleteUserById(int id);
}
