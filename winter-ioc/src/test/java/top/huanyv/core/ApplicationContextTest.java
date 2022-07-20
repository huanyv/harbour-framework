package top.huanyv.core;


import org.junit.Test;
import top.huanyv.core.dao.UserDao;
import top.huanyv.core.service.UserService;


class ApplicationContextTest {

    @Test
    public void getBean() {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());

        UserService userService = (UserService) app.getBean("userService");

        userService.getUser();

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());


        UserService userService = (UserService) app.getBean("userService");
        UserDao userDao = app.getBean(UserDao.class);
        userService.getUser();
    }
}