package top.huanyv.bean.test.ioc.service.impl;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.exception.BeansException;
import top.huanyv.bean.ioc.InitializingBean;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.dao.UserDao;
import top.huanyv.bean.test.ioc.service.UserService;

/**
 * @author huanyv
 * @date 2022/11/18 14:25
 */
@Bean(prototype = true)
public class UserServiceImpl implements UserService, InitializingBean {

    @Inject("userDao")
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    @Override
    public void afterPropertiesSet() throws BeansException {
        System.out.println("初始化...................");
    }
}
