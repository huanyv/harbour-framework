package top.huanyv.bean.test;

import org.junit.Test;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.test.aop.AdminService;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.factory.MapperFactoryBean;
import top.huanyv.bean.test.ioc.controller.UserController;
import top.huanyv.bean.test.ioc.dao.UserDao;

import java.util.Arrays;

/**
 * @author huanyv
 * @date 2022/11/18 14:18
 */
public class ApplicationContextTest {

    @Test
    public void testIOC() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.ioc");
        UserController userController = (UserController) app.getBean("userController");
        User user = userController.getUserById(2);
        System.out.println("user = " + user);

        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));
        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));
    }

    @Test
    public void testAOP() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.aop");
        AdminService adminService = app.getBean(AdminService.class);
        User user = adminService.getUser();
        System.out.println(user);
    }

    @Test
    public void testFactoryBean() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.factory");

        UserDao userDao = app.getBean(UserDao.class);
        System.out.println("userDao = " + userDao);
        System.out.println("userDao.getClass() = " + userDao.getClass());

        System.out.println("app.getBean(\"&mapperFactoryBean\") = " + app.getBean("&mapperFactoryBean"));
    }

    @Test
    public void testWeave() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.weave");
        UserDao userDao = app.getBean(UserDao.class);
        System.out.println(userDao);
    }
}
