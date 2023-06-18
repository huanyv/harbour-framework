package top.huanyv.bean.test.ioc.dao.impl;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.dao.UserDao;

/**
 * @author huanyv
 * @date 2022/11/18 14:23
 */
@Component("adminDao")
public class AdminDaoImpl implements UserDao {


    @Override
    public User getUserById(Integer id) {
        return new User(id, "admin", "123456", "女", "1212@qq.com");
    }
}
