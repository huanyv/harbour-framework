package top.huanyv.jdbc.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import sun.net.ConnectionResetException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import top.huanyv.jdbc.anno.Select;
import top.huanyv.jdbc.anno.Update;

import javax.sql.DataSource;
import javax.xml.bind.PrintConversionEvent;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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


    private MapperScanner mapperScanner;

    public void setMapperScanner(MapperScanner mapperScanner) {
        this.mapperScanner = mapperScanner;
    }

    public MapperScanner getMapperScanner() {
        return mapperScanner;
    }

    public <T> T getMapper(Class<T> clazz) {
        for (Map.Entry<String, Object> entry : this.mapperScanner.getMappers().entrySet()) {
            Object mapper = entry.getValue();
            if (clazz.isInstance(mapper)) {
                return (T) mapper;
            }
        }
        return null;
    }





}
