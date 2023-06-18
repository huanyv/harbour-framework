package top.huanyv.bean.test.ioc.service.impl;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.annotation.Prototype;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.dao.UserDao;
import top.huanyv.bean.test.ioc.service.UserService;

/**
 * @author huanyv
 * @date 2022/11/18 14:25
 */
@Component
@Prototype
public class UserServiceImpl implements UserService {

    @Inject("userDao")
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

}
