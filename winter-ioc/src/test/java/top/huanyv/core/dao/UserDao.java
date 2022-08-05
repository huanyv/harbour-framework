package top.huanyv.core.dao;

import top.huanyv.core.aop.TestAop;
import top.huanyv.ioc.aop.Aop;

/**
 * @author admin
 * @date 2022/8/5 10:06
 */

public interface UserDao {
    void getUser();

    void getUserById();
}
