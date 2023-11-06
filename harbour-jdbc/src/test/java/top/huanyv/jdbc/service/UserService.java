package top.huanyv.jdbc.service;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.entity.User;
import top.huanyv.jdbc.support.TransactionAop;

import java.util.List;

/**
 * @author huanyv
 * @date 2023/5/15 13:59
 */
@Bean
@Aop(TransactionAop.class)
public class UserService {

    @Inject
    private UserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public List<User> getUsers() {
        userDao.getUserById(1);
        int i = 10 /0;
        return userDao.getUsers();
    }

}
