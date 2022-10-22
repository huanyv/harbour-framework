package top.huanyv.core.test.dao.impl;

import top.huanyv.core.test.dao.UserDao;
import top.huanyv.ioc.anno.Component;

/**
 * @author huanyv
 * @date 2022/10/20 21:00
 */
@Component
public class UserDaoImpl implements UserDao {
    @Override
    public void getUser() {
        try {
            System.out.println(this.getClass().getMethod("getUser") + "执行");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
