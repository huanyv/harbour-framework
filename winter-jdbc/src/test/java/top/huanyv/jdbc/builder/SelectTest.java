package top.huanyv.jdbc.builder;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;

import java.util.Arrays;

public class SelectTest extends TestCase {

    public void testFrom() {
        QueryBuilder queryBuilder = new Select().from(User.class)
                .where().and("uid = 1").and("username = 'admin'")
                .limit(10);
        String sql = queryBuilder.sql();
        System.out.println("queryBuilder.sqlBuilder.arguments = " + Arrays.toString(queryBuilder.getArguments()));
        System.out.println(sql);
    }



    ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();

    public void test03() {
        stringThreadLocal.set("123");
        System.out.println("main = " + stringThreadLocal.get());

        new Thread(new Runnable() {
            @Override
            public void run() {
//                stringThreadLocal.set("abd");
                System.out.println("thread = " + stringThreadLocal.get());
            }
        }).start();
    }
}