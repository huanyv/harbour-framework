package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.tools.utils.ClassLoaderUtil;

import java.io.InputStream;
import java.util.List;

public class QueryBuilderTest extends TestCase {

    public void testSelectOne() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        User user = new Select().from(User.class).where(q -> q.and("uid = ?", 1))
//                .and("uid = ?", 1)
                .selectRow();
        System.out.println(user);
        List<User> users = new Select().from(User.class).selectList();
        System.out.println("users = " + users);
        Long count = new Select("count(*)").from(User.class).selectValue(Long.class);
        System.out.println("count = " + count);
    }

    public void testPage() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        Page<User> page = new Select().from(User.class).page(12, 5);
        System.out.println("page = " + page);
    }

    public void testUpdate() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        int update = new QueryBuilder().append("insert into user").append("(username, password, sex, email)")
                .append("values(?, ?, ?, ?)", "zhangsan2k", "abc", "男", "1232qq.com").update();
        System.out.println(update);
    }
}