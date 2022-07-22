package top.huanyv.jdbc.core;


import org.junit.Test;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.core.interfaces.UserDao;

import java.io.InputStream;
import java.util.List;

public class SqlSessionTest {

    @Test
    public void testGetMapper() {

        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        userDao.getUserById(1);
        List<User> user = userDao.getUser();
        System.out.println(user);
    }
}