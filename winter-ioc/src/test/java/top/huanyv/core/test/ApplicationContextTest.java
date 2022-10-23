package top.huanyv.core.test;


import org.junit.Test;
import top.huanyv.core.test.controller.UserController;
import top.huanyv.ioc.aop.AopContext;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.utils.AopUtil;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext app = new AnnotationConfigApplicationContext(this.getClass().getPackage().getName());
        UserController userController = app.getBean(UserController.class);
        System.out.println("AopUtil.getTargetClass(userController) = " + AopUtil.getTargetClass(userController));
//        userController.getUser();
//        System.out.println("==================================================");
//        userController.getUser02();
    }
}