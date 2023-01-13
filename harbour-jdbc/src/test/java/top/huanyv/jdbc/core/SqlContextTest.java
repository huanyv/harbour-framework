package top.huanyv.jdbc.core;

import org.junit.Test;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.dao.impl.UserDaoImpl;
import top.huanyv.jdbc.entity.User;
import top.huanyv.jdbc.util.Page;
import top.huanyv.tools.utils.ClassLoaderUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SqlContextTest {

    public JdbcConfigurer getConfig() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        return JdbcConfigurer.create(inputStream);
    }

    @Test
    public void selectRow() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        User user = sqlContext.selectRow(User.class, "select * from t_user where id = ?", 1);
        System.out.println("user = " + user);
    }

    @Test
    public void selectList() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        List<User> users = sqlContext.selectList(User.class, "select * from t_user");
        System.out.println("users = " + users);
    }

    @Test
    public void selectMap() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        Map<String, Object> user = sqlContext.selectMap("select * from t_user");
        System.out.println("user = " + user);
    }

    @Test
    public void selectListMap() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        List<Map<String, Object>> users = sqlContext.selectListMap("select * from t_user");
        System.out.println("users = " + users);
    }

    @Test
    public void selectValue() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        Object o = sqlContext.selectValue("select max(id) from t_user");
        System.out.println(o);
        System.out.println("o.getClass() = " + o.getClass());
    }

    @Test
    public void selectPage() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        Page<User> page = new Page<>(1,3);
        sqlContext.selectPage(page, User.class, "select * from t_user");
        System.out.println("page = " + page);
    }

    @Test
    public void selectPageMap() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        Page<Map<String, Object>> page = new Page<>(1,3);
        sqlContext.selectPageMap(page,  "select * from t_user");
        System.out.println("page = " + page);
    }

    @Test
    public void selectCount() {
    }

    @Test
    public void update() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        String sql = "insert into t_user (username, password) values(?, ?)";
        int count = sqlContext.update(sql, "user", "1234");
        System.out.println(count);
    }

    @Test
    public void update01() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        User user = new User();
        user.setUsername("user3");
        user.setPassword("1111");

        String sql = "insert into t_user (username, password) values(#{username}, #{password})";
        int count = sqlContext.update(sql, user);
        System.out.println(count);
    }

    @Test
    public void update02() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "user4");
        map.put("password", "2222");

        String sql = "insert into t_user (username, password) values(#{username}, #{password})";
        int count = sqlContext.update(sql, map);
        System.out.println(count);
    }

    @Test
    public void update03() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        // 这个出现的作用是当你的参数，中间有其它干扰，可以避开
        String sql = "insert into t_user (username, password) values(#{arg0}, #{arg2})";
        int count = sqlContext.update(sql, "user5",new Object() ,"44444");
        System.out.println(count);
    }


    @Test
    public void insert() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        String sql = "insert into t_user (username, password) values(?, ?)";
        long id = sqlContext.insert(sql, "user2", "1234");
        System.out.println(id);
    }

    @Test
    public void testDao() {
        getConfig();
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        UserDao userDao = sqlContext.getDao(UserDaoImpl.class);
        List<User> users = userDao.selectAll();
        System.out.println("users = " + users);
    }
}