package top.huanyv.bean.test.ioc.dao;

import top.huanyv.bean.test.entity.User;

/**
 * @author huanyv
 * @date 2022/11/18 14:22
 */
public interface UserDao {

    User getUserById(Integer id);

}
