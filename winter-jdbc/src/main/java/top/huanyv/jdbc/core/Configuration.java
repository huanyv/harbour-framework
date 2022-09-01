package top.huanyv.jdbc.core;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.utils.Assert;

import javax.sql.DataSource;

/**
 * @author huanyv
 * @date 2022/9/1 19:39
 */
public class Configuration {

    private DataSource dataSource;

    private String scanPackages;

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * 单例
     */
    private static Configuration configuration;

    private Configuration() { }

    private static class SingletonHolder {
        private static final Configuration CONFIGURATION = new Configuration();
    }

    public static Configuration create() {
        return SingletonHolder.CONFIGURATION;
    }

    public static Configuration create(InputStream inputStream) {
        Assert.notNull(inputStream);
        Configuration config = SingletonHolder.CONFIGURATION;
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            config.setDriverClassName(properties.getProperty("driverClassName"));
            config.setUrl(properties.getProperty("url"));
            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));
            config.setScanPackages(properties.getProperty("mapper.scan"));
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

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            SimpleDataSource simpleDataSource = new SimpleDataSource();
            simpleDataSource.setDriverClassName(this.driverClassName);
            simpleDataSource.setUrl(this.url);
            simpleDataSource.setUsername(this.username);
            simpleDataSource.setPassword(this.password);
            dataSource = simpleDataSource;
        }
        return dataSource;
    }

}
