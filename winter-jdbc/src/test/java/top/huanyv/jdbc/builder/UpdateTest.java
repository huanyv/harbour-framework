package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.utils.ClassLoaderUtil;

import java.io.InputStream;

public class UpdateTest extends TestCase {

    public void testWhere() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
//        SqlSession sqlSession = SqlSessionFactory.openSession(inputStream);
        int update = new Update(User.class).append("username = ?", "lisi222")
                .append(true, "sex = ?", "男").where().and("uid = ?", 7).update();
        System.out.println(update);

    }
}