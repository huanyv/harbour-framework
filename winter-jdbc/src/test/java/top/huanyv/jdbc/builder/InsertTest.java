package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.tools.utils.ClassLoaderUtil;

import java.io.InputStream;
import java.util.Arrays;

public class InsertTest extends TestCase {

    public void testColumns() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        QueryBuilder insert = new Insert(User.class).columns("username", "password", "sex", "email").values("zha", "ccc", "女", "123@163.con");
        System.out.println("insert.sql() = " + insert.sql());
        System.out.println("insert.getArguments() = " + Arrays.toString(insert.getArguments()));
        System.out.println("insert.update() = " + insert.update());

    }
}