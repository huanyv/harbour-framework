package top.huanyv.jdbc.core;

import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.jdbc.handler.BeanHandler;
import top.huanyv.jdbc.handler.BeanListHandler;
import top.huanyv.jdbc.handler.ScalarHandler;

import javax.sql.DataSource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

public class JdbcTemplateTest extends TestCase {

    public void testQuery() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        DataSource dataSource = SimpleDataSource.createDataSource(properties);

        Connection connection = dataSource.getConnection();


        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "select * from user where uid = ?";
        User user = jdbcTemplate.query(connection, sql, new BeanHandler<>(User.class), 1);
        System.out.println("user = " + user);

        List<User> users = jdbcTemplate.query(connection, "select * from user", new BeanListHandler<>(User.class));
        users.stream().forEach(System.out::println);

        Long count = jdbcTemplate.query(connection, "select count(*) from user", new ScalarHandler<>(Long.class));
        System.out.println("count = " + count);

        Number avg = jdbcTemplate.query(connection, "select avg(uid) from user", new ScalarHandler<>(Number.class));
        System.out.println("avg = " + avg);

        String username = jdbcTemplate.query(connection, "select username from user where uid = ?", new ScalarHandler<>(String.class), 1);
        System.out.println("username = " + username);
    }

    public void testUpdate() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        DataSource dataSource = SimpleDataSource.createDataSource(properties);

        Connection connection = dataSource.getConnection();

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "update user set username = ? where uid = ?";
        int update = jdbcTemplate.update(connection, sql, "lisi", 7);
        System.out.println("update = " + update);
    }
}