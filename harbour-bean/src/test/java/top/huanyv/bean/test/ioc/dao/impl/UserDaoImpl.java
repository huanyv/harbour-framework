package top.huanyv.bean.test.ioc.dao.impl;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.dao.UserDao;

/**
 * @author huanyv
 * @date 2022/11/18 14:23
 */
@Component("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public User getUserById(Integer id) {
        return new User(id, "user", "123", "男", "2233@qq.com");
    }
}
