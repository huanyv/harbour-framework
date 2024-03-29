package top.huanyv.jdbc.core.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池
 *
 * @author huanyv
 * @date 2022/10/11 19:15
 */
public class ConnectionPool {

    private final List<ConnectionDecorator> connections = new ArrayList<>();

    private String url;

    private String username;

    private String password;

    /**
     * 最大连接数
     */
    private int maxActive = 15;

    /**
     * 最小空闲数
     */
    private int minIdle = 5;

    /**
     * 初始大小
     */
    private int initialSize = 10;

    public ConnectionPool(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        // 初始化连接池
        for (int i = 0; i < this.initialSize; i++) {
            try {
                this.connections.add(newConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 回收连接
     *
     * @param connection 连接
     */
    public synchronized void recycle(ConnectionDecorator connection) throws SQLException {
        // 防止多次调用close()方法出现重复连接
        if (contains(connection)) {
            return;
        }
        if (getActiveCount() >= this.maxActive) {
            connection.realClose();
            return;
        }
        this.connections.add(connection);
    }

    /**
     * 从池子中获取一条连接
     *
     * @return {@link Connection}
     */
    public synchronized Connection getConnection() throws SQLException {

        // 空闲连接数
        if (getActiveCount() <= 0) {
            for (int i = 0; i <= minIdle; i++) {
                this.connections.add(newConnection());
            }
        }

        // 连接池非空
        ConnectionDecorator connectionDecorator = this.connections.get(0);
        this.connections.remove(connectionDecorator);
        connectionDecorator.open();
        return connectionDecorator;
    }

    /**
     * 创建新装饰器连接
     *
     * @return {@link ConnectionDecorator}
     * @throws SQLException sqlexception异常
     */
    public ConnectionDecorator newConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        return new ConnectionDecorator(connection, this);
    }

    public boolean contains(ConnectionDecorator connection) {
        return this.connections.contains(connection);
    }

    public int getActiveCount() {
        return this.connections.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("index = ").append(this.connections.size()).append("\n");
        for (int i = 0; i < connections.size(); i++) {
            result.append(i + 1).append(":\t").append(connections.get(i).toString()).append("\n");
        }
        return result.toString();
    }
}
