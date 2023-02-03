package top.huanyv.bean.test.ioc.controller;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.test.aop.LogAspect;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.service.UserService;

/**
 * @author huanyv
 * @date 2022/11/18 14:26
 */
@Component
@Aop(LogAspect.class)
public class UserController {

    @Inject
    private UserService userService;

    public User getUserById(Integer id) {
        return userService.getUserById(id);
    }
}
