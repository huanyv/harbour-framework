package top.huanyv.jdbc.core;

import junit.framework.TestCase;
import top.huanyv.jdbc.builder.Delete;
import top.huanyv.jdbc.builder.Select;
import top.huanyv.jdbc.builder.Update;
import top.huanyv.jdbc.core.dao.BookDao;
import top.huanyv.jdbc.core.dao.UserDao;
import top.huanyv.jdbc.core.entity.User;

import java.io.InputStream;
import java.util.List;

public class SqlContextFactoryTest extends TestCase {

    public void testGetSqlContext() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);
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

    public void test02() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        UserDao userDao = sqlContext.getDao(UserDao.class);
        System.out.println("userDao = " + userDao);
        BookDao bookDao = sqlContext.getDao(BookDao.class);
        System.out.println("bookDao = " + bookDao);
//        List<User> users = userDao.getUser();
//        users.stream().forEach(System.out::println);
    }

    public void test03() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        JdbcConfigurer.create(inputStream);

        SqlContext sqlContext = SqlContextFactory.getSqlContext();


        try {
            sqlContext.beginTransaction();

            int update = new Update(User.class)
                    .append("username = ?", "lisi2233")
                    .where().append("uid = ?", 7)
                    .update();
            System.out.println("update = " + update);

//            int i = 10 / 0;

            int delete = new Delete().from(User.class).where().append("uid = ?", 12).update();
            System.out.println("delete = " + delete);

            sqlContext.commit();
        } catch (Exception e) {
            sqlContext.rollback();
            e.printStackTrace();
        }


    }

}