package top.huanyv.jdbc.dao;

import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.entity.User;

/**
 * @author huanyv
 * @date 2023/1/13 15:52
 */
public interface UserDao extends BaseDao<User> {
    User getUserById(int id);
}
