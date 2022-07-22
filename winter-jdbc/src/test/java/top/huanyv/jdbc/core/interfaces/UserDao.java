package top.huanyv.jdbc.core.interfaces;

import top.huanyv.jdbc.anno.Mapper;
import top.huanyv.jdbc.anno.Select;
import top.huanyv.jdbc.core.entity.User;

import java.util.List;

/**
 * @author admin
 * @date 2022/7/21 17:09
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = ?")
    User getUserById(Integer id);


    @Select("select * from user")
    List<User> getUser();
}
