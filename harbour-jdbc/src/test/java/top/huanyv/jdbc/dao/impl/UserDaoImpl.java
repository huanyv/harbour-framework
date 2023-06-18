package top.huanyv.jdbc.dao.impl;

import top.huanyv.jdbc.annotation.*;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.entity.User;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/1/13 16:04
 */
@Dao
public class UserDaoImpl implements UserDao {

    @Override
    @Select("select * from t_user")
    public Page<User> pageUser() {
        return null;
    }

    @Override
    @Select("select * from t_user where id = #{arg0}")
    public User getUserById(int id) {
        return null;
    }

    @Override
    @Select("select * from t_user")
    public List<User> getUsers() {
        return null;
    }

    @Override
    @Update("UPDATE t_user SET username=#{username}, password=#{password} WHERE id=#{id}")
    public int updateUserById(User user) {
        return 0;
    }

    @Override
    @Insert("INSERT INTO t_user (username, password) VALUES(#{username}, #{password});")
    public int insertUser(User user) {
        return 0;
    }

    @Override
    @Delete("delete from t_user where id = ?")
    public int deleteUserById(int id) {
        return 0;
    }
}
