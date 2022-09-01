package top.huanyv.jdbc.core;


import org.junit.Test;
import top.huanyv.jdbc.core.dao.UserDao;
import top.huanyv.jdbc.dao.UserDaoImpl;

import java.io.InputStream;

public class SqlSessionTest {

    @Test
    public void testGetMapper() {

        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);

        UserDao userDao = sqlSession.getMapper(UserDao.class);

        System.out.println("userDao.getUserById(1) = " + userDao.getUserById(1));
//        List<User> user = userDao.getUser();
//        System.out.println(user);
//        System.out.println("userDao.getUserCount() = " + userDao.getUserCount());
//        System.out.println("userDao.getUserNameById(1) = " + userDao.getUserNameById(1));
    }

    @Test
    public void test2() {
        new UserDaoImpl().selectAll();
    }
}