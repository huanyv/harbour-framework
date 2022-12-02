package top.huanyv.jdbc.core.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author huanyv
 * @date 2022/10/11 19:14
 */

public class SimpleDataSource implements DataSource {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private ConnectionPool connectionPool;

    public SimpleDataSource() {
    }

    public static DataSource createDataSource(Properties properties) {
        return createDataSource((Map) properties);
    }
    public static DataSource createDataSource(Map map) {
        String driverClassName = (String) map.get("driverClassName");
        String url = (String) map.get("url");
        String username = (String) map.get("username");
        String password = (String) map.get("password");

        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.initConnectionPool();
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool == null) {
            initConnectionPool();
        }
        return connectionPool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    /**
     * 初始化连接池
     */
    public void initConnectionPool() {
        connectionPool = new ConnectionPool(this.url, this.username, this.password);
    }

    /**
     * 加载数据库驱动程序
     *
     * @param driverClassName 驱动程序类名称
     */
    public void loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        loadDriver(driverClassName);
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
