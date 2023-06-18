package top.huanyv.jdbc.core.datasource;

import com.mysql.jdbc.Driver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ConnectionPoolTest {

    private ConnectionPool connectionPool;

    @Before
    public void before() throws ClassNotFoundException {
        this.connectionPool = new ConnectionPool("jdbc:mysql://localhost:3306/test?useSSL=false", "root", "2233");
        Class.forName(Driver.class.getName());
    }

    @Test
    public void recycle() throws SQLException {
        Connection connection = this.connectionPool.getConnection();
        System.out.println(connection);
        this.connectionPool.recycle((ConnectionDecorator) connection);
        System.out.println(this.connectionPool.toString());
    }
    @Test
    public void getConnection() throws SQLException {
        Connection connection = this.connectionPool.getConnection();
        System.out.println(connection);
    }

    @Test
    public void contains() throws SQLException {
        ConnectionDecorator connection = (ConnectionDecorator) this.connectionPool.getConnection();
        Assert.assertFalse(this.connectionPool.contains(connection));
        this.connectionPool.recycle(connection);
        Assert.assertTrue(this.connectionPool.contains(connection));
    }

    @Test
    public void getActiveCount() {
        int activeCount = this.connectionPool.getActiveCount();
        System.out.println("activeCount = " + activeCount);
    }

    @Test
    public void testToString() {
        System.out.println(this.connectionPool.toString());
    }
}