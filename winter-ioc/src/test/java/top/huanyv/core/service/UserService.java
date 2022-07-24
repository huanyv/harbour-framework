package top.huanyv.core.service;

import top.huanyv.ioc.anno.Autowired;
import top.huanyv.ioc.anno.Component;
import top.huanyv.core.dao.UserDao;

/**
 * @author admin
 * @date 2022/7/19 10:40
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookService bookService;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void getUser() {
        userDao.getUser();
        System.out.println("service...");
    }


    public BookService getBookService() {
        System.out.println("bookService.toString() = " + bookService.toString());
        return bookService;
    }

    @Override
    public String toString() {
        return "userservice";
    }
}
