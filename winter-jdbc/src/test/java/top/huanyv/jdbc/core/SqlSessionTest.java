package top.huanyv.jdbc.core;


import org.junit.Test;
import top.huanyv.jdbc.core.interfaces.UserDao;

public class SqlSessionTest {

    @Test
    public void testGetMapper() {
        SqlSession sqlSession = new SqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        System.out.println("userDao = " + userDao);
    }
}