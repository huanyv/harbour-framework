package top.huanyv.core.test;


import org.junit.Test;
import top.huanyv.core.test.config.UserFactory;
import top.huanyv.core.test.controller.UserController;
import top.huanyv.core.test.entity.User;
import top.huanyv.core.test.service.BookService;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.aop.AopContext;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.utils.AopUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext app = new AnnotationConfigApplicationContext(this.getClass().getPackage().getName());
        /*
        UserController userController = app.getBean(UserController.class);
//        System.out.println("AopUtil.getTargetClass(userController) = " + AopUtil.getTargetClass(userController));
        userController.getUser();
        System.out.println("==================================================");
        userController.getUser02();
        */

         /*
        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));
        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));
          */

        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));

        System.out.println("app.getBeanDefinitionNames() = " + Arrays.toString(app.getBeanDefinitionNames()));

        app.register(UserFactory.class, "lisi");
        app.refresh();

        System.out.println("app.getBeanDefinitionNames() = " + Arrays.toString(app.getBeanDefinitionNames()));

        System.out.println("app.getBean(User.class) = " + app.getBean(User.class));
        System.out.println("app.getBean(User.class) = " + app.getBean(User.class));

        System.out.println("app.getBean(\"&user\") = " + app.getBean("&userFactory"));
        System.out.println("app.getBean(\"&user\") = " + app.getBean("&userFactory"));

    }
}