package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.tools.utils.ClassLoaderUtil;

import java.io.InputStream;
import java.util.function.Consumer;

public class DeleteTest extends TestCase {

    public void testFrom() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);
        QueryBuilder delete = new Delete().from(User.class).where(conditionBuilder -> conditionBuilder.and("uid = ?", 5));
        System.out.println("delete.update() = " + delete.update());
        System.out.println("delete.sql() = " + delete.sql());
    }
}