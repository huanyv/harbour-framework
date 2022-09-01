package top.huanyv.jdbc.core;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.extend.SimpleDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class SqlContextFactoryTest extends TestCase {

    public void testGetSqlContext() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        Configuration.create(inputStream);
        SqlContext sqlContext = SqlContextFactory.getSqlContext();

        try {
            sqlContext.beginTransaction();
            int update = sqlContext.update("update user set username = ? where uid = ?", "lisi", 7);
            System.out.println("update = " + update);
//            int i = 10 / 0;
            int delete = sqlContext.update("delete from user where uid = ?", 9);
            System.out.println("delete = " + delete);

            sqlContext.commit();
        } catch (Exception e) {
            sqlContext.rollback();
            e.printStackTrace();
        }


    }
}