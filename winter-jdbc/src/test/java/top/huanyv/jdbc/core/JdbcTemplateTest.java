package top.huanyv.jdbc.core;
import java.io.PrintWriter;

import com.mysql.jdbc.Driver;
import junit.framework.TestCase;
import top.huanyv.jdbc.core.entity.Menu;
import top.huanyv.jdbc.core.entity.User;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.jdbc.handler.*;
import top.huanyv.utils.StringUtil;

import javax.sql.DataSource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
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

    public void test01() throws Exception {
        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_blog?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("2233");

        Connection connection = dataSource.getConnection();

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "select * from sys_menu where id = 100";
        Menu menu = jdbcTemplate.query(connection, sql, new BeanHandler<>(Menu.class));
        System.out.println("menu = " + menu);

        List<Map<String, Object>> mapList = jdbcTemplate.query(connection, "select * from sys_menu", new MapListHandler());
        System.out.println("mapList = " + mapList);
    }

}