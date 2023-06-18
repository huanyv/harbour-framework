package top.huanyv.jdbc.bean;

import org.junit.Before;
import org.junit.Test;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.jdbc.core.pagination.PageHolder;
import top.huanyv.jdbc.dao.UserDao;
import top.huanyv.jdbc.entity.User;
import top.huanyv.jdbc.service.UserService;
import top.huanyv.tools.utils.ClassLoaderUtil;
import top.huanyv.tools.utils.PropertiesUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author huanyv
 * @date 2023/5/15 13:52
 */
public class ApplicationContextTest {

    @Before
    public void config() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();
        jdbcConfigurer.setMapUnderscoreToCamelCase(true);
        Properties properties = PropertiesUtil.load(inputStream);
        SimpleDataSource dataSource = (SimpleDataSource) SimpleDataSource.createDataSource(properties);
        jdbcConfigurer.setDataSource(dataSource);
    }

    @Test
    public void testBean() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.jdbc");

        UserDao userDao = app.getBean(UserDao.class);
        System.out.println("userDao = " + userDao);
        System.out.println("userDao.getClass() = " + userDao.getClass());

        User user = new User(1, "admin", "1111");
        userDao.updateUserById(user);
        User user2 = new User( "haha", "1111");
        userDao.insertUser(user2);

        userDao.deleteUserById(2);
    }

    @Test
    public void testTransactionAop() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.jdbc");
        UserService userService = app.getBean(UserService.class);
        User user = userService.getUserById(1);
        System.out.println("user = " + user);

        userService.getUsers();
    }

    @Test
    public void testPage() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.jdbc");
        UserDao userDao = app.getBean(UserDao.class);
        PageHolder.startPage(2, 10);
        Page<User> page = userDao.pageUser();
        System.out.println(page.getData());
    }

    @Test
    public void testBaseDao() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.jdbc");
        UserDao userDao = app.getBean(UserDao.class);
        System.out.println(userDao.selectAll());
        System.out.println(userDao.selectById(1));
        User user = new User(1, "admin", "1111");
        userDao.updateById(user);
        User user2 = new User( "haha", "1111");
        userDao.insert(user2);

        userDao.deleteById(2);
    }

}
