package top.huanyv.jdbc.core.config;

import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.jdbc.core.MapperScanner;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.jdbc.core.SqlSessionTest;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author admin
 * @date 2022/7/23 16:49
 */
@Configuration
public class MapperConfig {


    @Bean
    public DataSource dataSource() throws IOException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return SimpleDataSource.createDataSource(properties);
    }
    @Bean
    public MapperScanner mapperScanner() {
        return new MapperScanner(SqlSessionTest.class.getPackage().getName());
    }

}
