package top.huanyv.bean.test.ioc.dao.impl;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.test.aop.LogAspect;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.dao.UserDao;

/**
 * @author huanyv
 * @date 2022/11/18 14:23
 */
@Bean("userDao")
public class UserDaoImpl implements UserDao {

    @Inject
    private AdminDaoImpl adminDao;

    @Override
    @Aop(LogAspect.class)
    public User getUserById(Integer id) {
        return new User(id, "user", "123", "男", "2233@qq.com");
    }
}
