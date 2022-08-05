package top.huanyv.core.service;

import top.huanyv.core.aop.TestAop;
import top.huanyv.core.dao.UserDao;
import top.huanyv.ioc.anno.Autowired;
import top.huanyv.ioc.anno.Component;
import top.huanyv.core.dao.UserDaoImpl;
import top.huanyv.ioc.aop.Aop;

/**
 * @author admin
 * @date 2022/7/19 10:40
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private BookService bookService;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

//    @Aop(TestAop.class)
    public void getUser() {
//        System.out.println("userDao = " + userDao);
        userDao.getUser();
    }

    public void getUserById() {
        userDao.getUserById();
    }


//    public BookService getBookService() {
//        System.out.println("bookService.toString() = " + bookService.toString());
//        return bookService;
//    }

    @Override
    public String toString() {
        return "UserService{" +
                "userDao=" + userDao +
                '}';
    }
}
