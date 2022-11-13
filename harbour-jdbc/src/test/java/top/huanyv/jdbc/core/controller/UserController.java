package top.huanyv.jdbc.core.controller;

import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.jdbc.core.dao.UserDao;
import top.huanyv.jdbc.core.entity.User;


/**
 * @author admin
 * @date 2022/7/23 16:39
 */
public class UserController {

    public static void main(String[] args) {

        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.jdbc");

        UserDao userDao = app.getBean(UserDao.class);
        User userById = userDao.getUserById(1);
        System.out.println("userById = " + userById);


    }

}
