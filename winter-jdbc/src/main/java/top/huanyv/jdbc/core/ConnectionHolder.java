package top.huanyv.jdbc.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2022/8/2 10:33
 */
public class ConnectionHolder {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static void set(Connection connection) {
        threadLocal.set(connection);
    }

    public static Connection getCurConnection() {
        return threadLocal.get();
    }


    public static void close() {
        Connection connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
