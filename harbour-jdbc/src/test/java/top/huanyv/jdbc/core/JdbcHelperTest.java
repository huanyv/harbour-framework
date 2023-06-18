package top.huanyv.jdbc.core;

import org.junit.Before;
import org.junit.Test;
import sun.awt.IconInfo;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.entity.User;
import top.huanyv.jdbc.handler.BeanHandler;
import top.huanyv.tools.utils.ClassLoaderUtil;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.tools.utils.PropertiesUtil;
import top.huanyv.tools.utils.ResourceUtil;

import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class JdbcHelperTest {

    private DataSource dataSource;

    @Before
    public void before() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        Properties properties = PropertiesUtil.load(inputStream);
        DataSource dataSource = SimpleDataSource.createDataSource(properties);
        this.dataSource = dataSource;
    }

    @Test
    public void query() throws SQLException {
        JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
        User user = jdbcHelper.query("select * from t_user", new BeanHandler<>(User.class));
        System.out.println("user = " + user);
        Connection connection = this.dataSource.getConnection();
        JdbcHelper.close(connection);
    }

    @Test
    public void update() throws SQLException {
        JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
        jdbcHelper.update("INSERT INTO t_user (username, password) VALUES('user4', '2222');");
    }

    @Test
    public void insert() throws SQLException {
        JdbcHelper jdbcHelper = new JdbcHelper(dataSource);
        jdbcHelper.insert("UPDATE t_user SET username='user2', password='12345' WHERE id=26");
    }
}