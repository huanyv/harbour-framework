package top.huanyv.jdbc.mybatis.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.jdbc.mybatis.MapperScanner;
import top.huanyv.tools.utils.ClassLoaderUtil;
import top.huanyv.tools.utils.PropertiesUtil;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author huanyv
 * @date 2022/12/22 17:15
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MapperScanner mapperScanner() throws Exception {
        MapperScanner mapperScanner = new MapperScanner();
        mapperScanner.setConfigLocation("mybatis-config.xml");
        mapperScanner.setBasePackage("top.huanyv.jdbc.mybatis.mapper");

        // 使用三方连接池
        InputStream inputStream = ClassLoaderUtil.getInputStream("jdbc.properties");
        Properties properties = PropertiesUtil.load(inputStream);
        mapperScanner.setDataSource(DruidDataSourceFactory.createDataSource(properties));

        return mapperScanner;
    }

}
