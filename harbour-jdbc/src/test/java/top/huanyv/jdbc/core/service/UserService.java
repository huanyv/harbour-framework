package top.huanyv.jdbc.core.service;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.core.dao.UserDao;

import java.util.List;

/**
 * @author admin
 * @date 2022/7/23 16:37
 */
@Component
public class UserService {

    @Inject
    private UserDao userDao;

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }


    public int updateUsernameById(String username, Integer id) {
        return userDao.updateUsernameById(username, id);
    }

    public List<User> getUsers() {
        return userDao.getUser();
    }

}
