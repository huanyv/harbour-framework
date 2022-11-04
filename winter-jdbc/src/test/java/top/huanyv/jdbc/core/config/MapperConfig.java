package top.huanyv.jdbc.core.config;

import com.mysql.jdbc.Driver;
import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.support.DaoScanner;
import top.huanyv.jdbc.support.SqlContextFactoryBean;

/**
 * @author admin
 * @date 2022/7/23 16:49
 */
@Configuration
public class MapperConfig {

//    @Bean
//    public DataSource dataSource() throws IOException {
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream("jdbc.properties");
//        Properties properties = new Properties();
//        properties.load(inputStream);
//        return SimpleDataSource.createDataSource(properties);
//    }
//    @Bean
//    public MapperScanner mapperScanner() {
//        return new MapperScanner(SqlSessionTest.class.getPackage().getName());
//    }

    @Bean
    public DaoScanner daoScanner() throws Exception {
        // 类配置
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();
        jdbcConfigurer.setDriverClassName(Driver.class.getName());
        jdbcConfigurer.setUrl("jdbc:mysql://localhost:3306/temp?useSSL=false");
        jdbcConfigurer.setUsername("root");
        jdbcConfigurer.setPassword("2233");
        jdbcConfigurer.setScanPackages("com.huanyv.jdbc.core");

        // 文件配置
//        sqlSessionFactoryBean.setConfigLocation("jdbc.properties");
        return new DaoScanner();
    }

}
