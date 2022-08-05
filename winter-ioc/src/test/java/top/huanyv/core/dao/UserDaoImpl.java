package top.huanyv.core.dao;

import top.huanyv.core.aop.Test02Aop;
import top.huanyv.core.aop.TestAop;
import top.huanyv.core.service.UserService;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.aop.Aop;

import java.util.Arrays;

/**
 * @author admin
 * @date 2022/7/19 10:40
 */
@Component
//@Aop(TestAop.class)
public class UserDaoImpl implements UserDao {

    @Aop(TestAop.class)
    @Override
    public void getUser() {
        System.out.println("getuser");
    }

    @Aop(Test02Aop.class)
    @Override
    public void getUserById() {
        System.out.println("getUserById");
    }


}
