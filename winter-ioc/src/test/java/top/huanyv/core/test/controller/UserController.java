package top.huanyv.core.test.controller;

import top.huanyv.core.test.aop.Aop01;
import top.huanyv.core.test.aop.Aop02;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Inject;
import top.huanyv.ioc.aop.Aop;

/**
 * @author huanyv
 * @date 2022/10/20 21:03
 */
//@Component
@Aop(Aop01.class)
public class UserController {

    @Inject
    private UserService userService;

    public void getUser() {
        userService.getUser();
    }

    @Aop(Aop02.class)
    public void getUser02() {
        userService.getUser();
    }

}
