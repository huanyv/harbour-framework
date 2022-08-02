package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.SqlSession;
import top.huanyv.jdbc.core.SqlSessionFactory;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.utils.ClassLoaderUtil;

import java.io.InputStream;

public class DeleteTest extends TestCase {

    public void testFrom() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        QueryBuilder delete = new Delete().from(User.class).where().and("uid = ?", 4);
        System.out.println("delete.update() = " + delete.update());
        System.out.println("delete.sql() = " + delete.sql());
    }
}