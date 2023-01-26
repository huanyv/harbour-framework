package top.huanyv.jdbc.core.datasource;

import top.huanyv.tools.utils.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author huanyv
 * @date 2022/10/11 19:14
 */

public class SimpleDataSource extends AbstractDataSource {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private ConnectionPool connectionPool;

    public SimpleDataSource() {
    }

    public static DataSource createDataSource(Properties properties) {
        Assert.notNull(properties, "'properties' must not be null.");
        return createDataSource((Map) properties);
    }

    public static DataSource createDataSource(Map<String, String> map) {
        Assert.notNull(map, "'map' must not be null.");
        String driverClassName = map.get("driverClassName");
        String url = map.get("url");
        String username = map.get("username");
        String password = map.get("password");

        SimpleDataSource dataSource = new SimpleDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.initConnectionPool();
        return dataSource;
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (connectionPool == null) {
            initConnectionPool();
        }
        return connectionPool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
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

}
