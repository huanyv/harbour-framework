package com.book.mapper;

import com.book.pojo.User;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.annotation.Select;

/**
 * @author huanyv
 * @date 2022/11/17 17:50
 */
@Dao
public interface UserDao {

    @Select("select * from t_user where username = ? and password = ?")
    User getUserByUsernameAndPassword(String username, String password);

}
