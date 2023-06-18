package top.huanyv.jdbc.core.pagination;

import org.junit.Test;
import top.huanyv.jdbc.entity.User;

import java.net.SocketTimeoutException;

import static org.junit.Assert.*;

public class PagingSqlFactoryTest {

    @Test
    public void getPageSql() {
        String[] types = new String[]{"mysql", "oracle", "db2", "sqlite", "sqlserver"};
        for (String type : types) {
            PagingSqlHandler sqlHandler = PagingSqlFactory.getPageSql(type);
            if (sqlHandler != null) {
                String sql = sqlHandler.handle("select * from t_user", 1, 10);
                System.out.println(sql);
                String sql2 = sqlHandler.handle("select * from t_user", 2, 10);
                System.out.println(sql2);
            }
        }
    }

    @Test
    public void testPage() {
        Page<User> page = new Page<>(-1, 10);
        page.setTotal(100);
        Page<User> page2 = new Page<>(10000, 10);
        page.setTotal(100);
    }
}