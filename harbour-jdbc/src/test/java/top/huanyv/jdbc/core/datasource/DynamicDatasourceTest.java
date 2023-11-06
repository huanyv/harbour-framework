package top.huanyv.jdbc.core.datasource;

import com.mysql.jdbc.Driver;
import com.sun.media.sound.SoftTuning;
import org.junit.Before;
import org.junit.Test;
import top.huanyv.bean.utils.ClassLoaderUtil;
import top.huanyv.bean.utils.PropertiesUtil;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class DynamicDatasourceTest {

    private DynamicDatasource datasource;

    @Before
    public void before() {
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        Properties properties = PropertiesUtil.load(inputStream);
        SimpleDataSource dataSource1 = (SimpleDataSource) SimpleDataSource.createDataSource(properties);
        this.datasource = new DynamicDatasource();
        this.datasource.setDefaultDataSource(dataSource1);

        SimpleDataSource dataSource2 = new SimpleDataSource();
        dataSource2.setDriverClassName(Driver.class.getName());
        dataSource2.setUrl("jdbc:mysql://localhost:3306/ds");
        dataSource2.setUsername("root");
        dataSource2.setPassword("2233");
        this.datasource.setDataSource("ds2", dataSource2);
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = this.datasource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void getDataSource() {
        DataSource dataSource = this.datasource.getDataSource();
        System.out.println(dataSource);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = this.datasource.getDataSource("ds2");
        System.out.println(dataSource);
    }


    @Test
    public void getDefaultDataSource() {
        DataSource dataSource = this.datasource.getDefaultDataSource();
        System.out.println(dataSource);
    }

    @Test
    public void getDataSourceKey() {
        DataSourceKeyHolder.set("ds2");
        String key = this.datasource.getDataSourceKey();
        System.out.println(key);
    }
}