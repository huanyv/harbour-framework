package top.huanyv.jdbc.core.controller;

import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.jdbc.core.SqlSessionTest;
import top.huanyv.jdbc.core.service.UserService;
import top.huanyv.jdbc.extend.WinterBeanRegister;

import java.util.Arrays;

/**
 * @author admin
 * @date 2022/7/23 16:39
 */
public class UserController {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(SqlSessionTest.class.getPackage().getName()
                , WinterBeanRegister.class.getPackage().getName());

        System.out.println("app.getBeanDefinitionNames() = " + Arrays.toString(app.getBeanDefinitionNames()));

        UserService userService = app.getBean(UserService.class);

        System.out.println("userService.getUserById(1) = " + userService.getUserById(1));


//        System.out.println("userService.updateUsernameById(1, \"admin1\") = "
//                + userService.updateUsernameById("admin", 1));

    }

}
