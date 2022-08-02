package top.huanyv.jdbc.builder;

import com.mysql.jdbc.Driver;
import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.utils.ClassLoaderUtil;
import top.huanyv.utils.IoUtil;
import top.huanyv.utils.ResourceUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;

public class SelectTest extends TestCase {

    public void testFrom() {
        QueryBuilder queryBuilder = new Select().from(User.class)
                .where().and("uid = 1").and("username = 'admin'")
                .limit(10);
        String sql = queryBuilder.sql();
        System.out.println("queryBuilder.sqlBuilder.arguments = " + Arrays.toString(queryBuilder.sqlBuilder.getArguments()));
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