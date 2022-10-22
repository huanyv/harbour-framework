package top.huanyv.core.test;


import org.junit.Test;
import top.huanyv.core.test.controller.UserController;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext app = new AnnotationConfigApplicationContext(this.getClass().getPackage().getName());
        UserController userController = app.getBean(UserController.class);
        System.out.println(userController.getClass());
        userController.getUser();
        System.out.println("==================================================");
        userController.getUser02();
    }
}