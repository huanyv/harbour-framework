package top.huanyv.core.test.service.impl;

import top.huanyv.core.test.dao.UserDao;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Inject;

/**
 * @author huanyv
 * @date 2022/10/20 21:02
 */
@Component
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
