package top.huanyv.core.dao;

import top.huanyv.core.aop.Test02Aop;
import top.huanyv.core.aop.TestAop;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.aop.Aop;

/**
 * @author admin
 * @date 2022/7/19 10:40
 */
@Component
//@Aop(TestAop.class)
public class UserDaoImpl2 implements UserDao {

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
