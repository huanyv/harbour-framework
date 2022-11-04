package top.huanyv.core.test.service.impl;

import top.huanyv.core.test.dao.UserDao;
import top.huanyv.core.test.service.BookService;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Inject;
import top.huanyv.ioc.anno.Scope;
import top.huanyv.ioc.core.definition.BeanDefinition;

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
