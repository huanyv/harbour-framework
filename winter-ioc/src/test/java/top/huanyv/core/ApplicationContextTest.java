package top.huanyv.core;


import org.junit.Test;
import top.huanyv.core.dao.UserDao;
import top.huanyv.core.service.UserService;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;


public class ApplicationContextTest {

    @Test
    public void getBean() {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());


        UserService userService = (UserService) app.getBean("userService");
        UserDao userDao = app.getBean(UserDao.class);
        userService.getUser();

        System.out.println("app.getBean(\"string\") = " + app.getBean("string"));

    }

}