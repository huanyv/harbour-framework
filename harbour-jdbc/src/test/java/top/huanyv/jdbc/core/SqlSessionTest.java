package top.huanyv.jdbc.core;


import org.junit.Test;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.dao.UserDaoImpl;

import java.io.InputStream;

public class SqlSessionTest {

    @Test
    public void testGetMapper() {

//        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
//
//        UserDao userDao = sqlSession.getMapper(UserDao.class);
//
//        System.out.println("userDao.getUserById(1) = " + userDao.getUserById(1));
//        List<User> user = userDao.getUser();
//        System.out.println(user);
//        System.out.println("userDao.getUserCount() = " + userDao.getUserCount());
//        System.out.println("userDao.getUserNameById(1) = " + userDao.getUserNameById(1));
    }

    @Test
    public void test2() {
        UserDaoImpl userDao = new UserDaoImpl();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);

        Object user = userDao.selectById(1);
        System.out.println(user);
    }

    @Test
    public void test3() {
        UserDaoImpl userDao = new UserDaoImpl();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);

        User user = new User();
        user.setUsername("danni");
        user.setPassword("123");
        user.setSex("男");
        user.setEmail("123@qq.com");

        int insert = userDao.insert(user);

    }

    @Test
    public void test4() {
        UserDaoImpl userDao = new UserDaoImpl();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);

        User user = new User();
        user.setUid(7L);
        user.setUsername("lisi");
        user.setSex("女");

        int update = userDao.updateById(user);

    }
    @Test
    public void test5() {
        UserDaoImpl userDao = new UserDaoImpl();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);

        int deleteById = userDao.deleteById(13);
        System.out.println(deleteById);
    }

}