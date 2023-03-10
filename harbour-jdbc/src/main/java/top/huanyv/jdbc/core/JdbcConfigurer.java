package top.huanyv.jdbc.core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.jdbc.core.pagination.PagingSqlFactory;
import top.huanyv.jdbc.core.pagination.PagingSqlHandler;
import top.huanyv.tools.utils.Assert;

import javax.sql.DataSource;

/**
 * @author huanyv
 * @date 2022/9/1 19:39
 */
public class JdbcConfigurer {

    /**
     * 数据源
     */
    private DataSource dataSource;

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * 下线线转驼峰映射
     */
    private boolean mapUnderscoreToCamelCase;

    /**
     * 单例
     */
    private JdbcConfigurer() {
    }
    private static class SingletonHolder {
        private static final JdbcConfigurer CONFIGURATION = new JdbcConfigurer();
    }
    public static JdbcConfigurer create() {
        return SingletonHolder.CONFIGURATION;
    }

    public static JdbcConfigurer create(InputStream inputStream) {
        Assert.notNull(inputStream);
        JdbcConfigurer config = SingletonHolder.CONFIGURATION;
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            config.setDriverClassName(properties.getProperty("driverClassName"));
            config.setUrl(properties.getProperty("url"));
            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            SimpleDataSource ds = new SimpleDataSource();
            ds.setDriverClassName(this.driverClassName);
            ds.setUrl(this.url);
            ds.setUsername(this.username);
            ds.setPassword(this.password);
            dataSource = ds;
        }
        return dataSource;
    }

    public static String getPageSql(String sql, Page<?> page) {
        Connection connection = null;
        String databaseProductName = null;
        try {
            // 获取数据库型号
            connection = JdbcConfigurer.create().getDataSource().getConnection();
            databaseProductName = connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (databaseProductName == null) {
            throw new IllegalArgumentException("Database not recognized.");
        }
        // 获取分页SQL处理器
        PagingSqlHandler handler = PagingSqlFactory.getPageSql(databaseProductName);
        if (handler == null) {
            throw new IllegalArgumentException("The '" + databaseProductName + "' database type cannot be handled.");
        }
        return handler.handle(sql, page.getPageNum(), page.getPageSize());
    }

    /**
     * 是否开启了下划线转驼峰映射，数据库字段为下划线格式，对象属性为驼峰格式
     *
     * @return boolean
     */
    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
