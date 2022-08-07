package top.huanyv.core;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import top.huanyv.core.dao.UserDao;
import top.huanyv.core.dao.UserDaoImpl;
import top.huanyv.core.service.UserService;
import top.huanyv.ioc.aop.ProxyFactory;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.utils.AopUtil;
import top.huanyv.utils.ProxyUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;


public class ApplicationContextTest {

    @Test
    public void getBean() {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());

        UserService userService = app.getBean(UserService.class);
//        userService.getBookService();
        userService.getUser();
        System.out.println("------------------------------");
        userService.getUserById();

//        AnnotationConfigApplicationContext app
//                = new AnnotationConfigApplicationContext(ClassLoader.getSystemResourceAsStream("application.properties"));

//        UserService userService = (UserService) app.getBean("userService");
//        userService.getUser();
//
//        System.out.println("app.getBean(\"string\") = " + app.getBean("string"));
//        System.out.println("app.getBean(\"string\") = " + app.getBean("string2"));
//
//
//        BookService bookService = app.getBean(BookService.class);
//        System.out.println("bookService = " + bookService.getUserService());
//        System.out.println("userService = " + userService.getBookService());

    }

    @Test
    public void test01() throws Exception {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());
        UserService userService = app.getBean(UserService.class);
        UserDao userDao = app.getBean(UserDao.class);

        System.out.println("AopUtil.getTargetObject(userDao) = " + AopUtil.getTargetObject(userDao));
        System.out.println("AopUtil.getTargetObject(userService) = " + AopUtil.getTargetObject(userService));
        System.out.println("AopUtil.getTargetClass(userDao) = " + AopUtil.getTargetClass(userDao));
        System.out.println("AopUtil.getTargetClass(userService) = " + AopUtil.getTargetClass(userService));
    }


}