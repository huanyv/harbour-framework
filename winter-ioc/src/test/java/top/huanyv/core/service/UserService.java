package top.huanyv.core.service;

import top.huanyv.anno.Autowired;
import top.huanyv.anno.Component;
import top.huanyv.core.dao.UserDao;

/**
 * @author admin
 * @date 2022/7/19 10:40
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void getUser() {
        userDao.getUser();
        System.out.println("service...");
    }

}
