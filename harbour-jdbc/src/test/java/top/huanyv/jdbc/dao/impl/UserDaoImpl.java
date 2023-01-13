package top.huanyv.jdbc.dao.impl;

import top.huanyv.jdbc.annotation.Select;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.entity.User;

/**
 * @author huanyv
 * @date 2023/1/13 16:04
 */
public class UserDaoImpl implements UserDao {
    @Override
    @Select("select * from t_user where id = #{arg0}")
    public User getUserById(int id) {
        return null;
    }
}
