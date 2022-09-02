package top.huanyv.jdbc.core.dao;

import top.huanyv.jdbc.anno.Delete;
import top.huanyv.jdbc.anno.Dao;
import top.huanyv.jdbc.anno.Select;
import top.huanyv.jdbc.anno.Update;
import top.huanyv.jdbc.core.entity.User;

import java.util.List;

/**
 * @author admin
 * @date 2022/7/21 17:09
 */
@Dao
public interface UserDao {

    @Select("select * from user where uid = ?")
    User getUserById(Integer id);


    @Select("select * from user")
    List<User> getUser();


    @Select("select count(*) from user")
    long getUserCount();

    @Select("select username from user where uid = ?")
    String getUserNameById(int id);

    @Update("update user set username = ? where uid = ?")
    int updateUsernameById(String username, Integer id);

    @Delete("delete from user where uid = ?")
    int deleteUserById(Integer uid);
}
