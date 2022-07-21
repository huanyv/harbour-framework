package top.huanyv.jdbc.core;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * @author admin
 * @date 2022/7/21 17:31
 */
public class SqlSessionFactory {
    public static SqlSession openSession(InputStream inputStream) {
        SqlSession sqlSession = new SqlSession();

        return sqlSession;
    }
    public static SqlSession openSession(DataSource dataSource) {
        SqlSession sqlSession = new SqlSession();
        sqlSession.setDataSource(dataSource);
        return sqlSession;
    }
}
