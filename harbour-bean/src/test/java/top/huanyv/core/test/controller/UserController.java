package top.huanyv.core.test.controller;

import top.huanyv.bean.annotation.Scope;
import top.huanyv.core.test.aop.Aop01;
import top.huanyv.core.test.aop.Aop02;
import top.huanyv.core.test.service.UserService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;

/**
 * @author huanyv
 * @date 2022/10/20 21:03
 */
@Component
@Aop(Aop01.class)
@Scope("prototype")
public class UserController {

    @Inject("userService")
    private UserService userService;

    public void getUser() {
        userService.getUser();
    }

    @Aop(Aop02.class)
    public void getUser02() {
        userService.getUser();
    }

}
