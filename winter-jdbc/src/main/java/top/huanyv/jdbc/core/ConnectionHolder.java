package top.huanyv.jdbc.core;

import javax.sql.DataSource;
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

    private static DataSource dataSource;

    private static boolean isAutoClose = true;

    public static void setDataSource(DataSource dataSource) {
        ConnectionHolder.dataSource = dataSource;
    }

    public static void set(Connection connection) {
        threadLocal.set(connection);
    }

    public static Connection getCurConnection() {
        Connection connection = threadLocal.get();
        try {
            if (connection == null || connection.isClosed()) {
                connection = dataSource.getConnection();
                set(connection);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }


    public static void autoClose() {
        if (isAutoClose) {
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

    public static void handClose() {
        Connection connection = threadLocal.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void setAutoClose(boolean isAutoClose) {
        ConnectionHolder.isAutoClose = isAutoClose;
    }
}
