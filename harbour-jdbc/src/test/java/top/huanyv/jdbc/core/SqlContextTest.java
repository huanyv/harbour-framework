package top.huanyv.jdbc.core;

import org.junit.Before;
import org.junit.Test;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.dao.impl.UserDaoImpl;
import top.huanyv.jdbc.entity.User;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.bean.utils.ClassLoaderUtil;
import top.huanyv.bean.utils.PropertiesUtil;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SqlContextTest {

    @Before
    public void getConfig() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();
        jdbcConfigurer.setMapUnderscoreToCamelCase(true);
        Properties properties = PropertiesUtil.load(inputStream);
        SimpleDataSource dataSource = (SimpleDataSource) SimpleDataSource.createDataSource(properties);
        jdbcConfigurer.setDataSource(dataSource);
    }

    @Test
    public void selectRow() {
        SqlContext sqlContext = new SqlContextManager();
        User user = sqlContext.selectRow(User.class, "select * from t_user where id = ?", 1);
        System.out.println("user = " + user);
    }

    @Test
    public void selectList() {
        SqlContext sqlContext = new SqlContextManager();
        List<User> users = sqlContext.selectList(User.class, "select * from t_user");
        System.out.println("users = " + users);
    }

    @Test
    public void selectMap() {
        SqlContext sqlContext = new SqlContextManager();
        Map<String, Object> user = sqlContext.selectMap("select * from t_user");
        System.out.println("user = " + user);
    }

    @Test
    public void selectListMap() {
        SqlContext sqlContext = new SqlContextManager();
        List<Map<String, Object>> users = sqlContext.selectListMap("select * from t_user");
        System.out.println("users = " + users);
    }

    @Test
    public void selectValue() {
        SqlContext sqlContext = new SqlContextManager();
        Object o = sqlContext.selectValue("select max(id) from t_user");
        System.out.println(o);
        System.out.println("o.getClass() = " + o.getClass());
    }

    @Test
    public void selectPage() {
        SqlContext sqlContext = new SqlContextManager();
        Page<User> page = new Page<>(1, 3);
        sqlContext.selectPage(page, User.class, "select * from t_user");
        System.out.println("page = " + page);
    }

    @Test
    public void selectPageMap() {
        SqlContext sqlContext = new SqlContextManager();
        Page<Map<String, Object>> page = new Page<>(1, 3);
        sqlContext.selectPageMap(page, "select * from t_user");
        System.out.println("page = " + page);
    }

    @Test
    public void selectCount() {
        String sql = "select * from t_user";
        SqlContext sqlContext = new SqlContextManager();
        long l = sqlContext.selectCount(sql);
        System.out.println("l = " + l);
    }

    @Test
    public void update() {
        SqlContext sqlContext = new SqlContextManager();
        String sql = "insert into t_user (username, password) values(?, ?)";
        int count = sqlContext.update(sql, "user", "1234");
        System.out.println(count);
    }

    @Test
    public void update01() {
        SqlContext sqlContext = new SqlContextManager();
        User user = new User();
        user.setUsername("user3");
        user.setPassword("1111");

        String sql = "insert into t_user (username, password) values(#{username}, #{password})";
        int count = sqlContext.update(sql, user);
        System.out.println(count);
    }

    @Test
    public void update02() {
        SqlContext sqlContext = new SqlContextManager();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "user4");
        map.put("password", "2222");

        String sql = "insert into t_user (username, password) values(#{username}, #{password})";
        int count = sqlContext.update(sql, map);
        System.out.println(count);
    }

    @Test
    public void update03() {
        SqlContext sqlContext = new SqlContextManager();
        // 这个出现的作用是当你的参数，中间有其它干扰，可以避开
        String sql = "insert into t_user (username, password) values(#{arg0}, #{arg2})";
        int count = sqlContext.update(sql, "user5", new Object(), "44444");
        System.out.println(count);
    }


    @Test
    public void insert() {
        SqlContext sqlContext = new SqlContextManager();
        String sql = "insert into t_user (username, password) values(?, ?)";
        long id = sqlContext.insert(sql, "user2", "1234");
        System.out.println(id);
    }

    @Test
    public void testBaseDao() {
        SqlContext sqlContext = new SqlContextManager();
        UserDao userDao = sqlContext.getDao(UserDaoImpl.class);
        List<User> users = userDao.selectAll();
        System.out.println("users = " + users);
    }

    @Test
    public void testAnnotation() {
        SqlContext sqlContext = new SqlContextManager();
        UserDao userDao = sqlContext.getDao(UserDao.class);
        User user = userDao.getUserById(1);
        System.out.println("user = " + user);
    }

    @Test
    public void testTransaction() {
        String sql = "insert into t_user (username, password) values(#{arg0}, #{arg2})";
        SqlContext sqlContext = new SqlContextManager();
        // 测试提交
        sqlContext.beginTransaction();
        sqlContext.update(sql);
        sqlContext.commit();
        // 测试回滚
        sqlContext.beginTransaction();
        sqlContext.update(sql);
        sqlContext.rollback();
    }
}