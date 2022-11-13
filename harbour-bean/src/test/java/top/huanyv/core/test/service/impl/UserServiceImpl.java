package top.huanyv.core.test.service.impl;

import top.huanyv.core.test.dao.UserDao;
import top.huanyv.core.test.service.BookService;
import top.huanyv.core.test.service.UserService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.annotation.Scope;
import top.huanyv.bean.ioc.definition.BeanDefinition;

/**
 * @author huanyv
 * @date 2022/10/20 21:02
 */
@Component("userService")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

//    @Inject
    private BookService bookService;

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
