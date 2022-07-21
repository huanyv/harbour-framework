package top.huanyv.jdbc.core;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author admin
 * @date 2022/7/21 16:57
 */
public class SqlSession {
/*
    username=root
    password=root
    url=jdbc:mysql://localhost:3306/book
    driverClassName=com.mysql.jdbc.Driver
    initialSize=5
    maxActive=10
*/

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private QueryRunner queryRunner = new QueryRunner(dataSource);

    public <T> T getMapper(Class<T> clazz) {
        return ProxyFactory.getImpl(clazz, (proxy, method, args) -> {



            return toString();
        });
    }

}
