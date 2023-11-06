package top.huanyv.bean.test.factory;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.test.ioc.dao.UserDao;

/**
 * @author huanyv
 * @date 2022/11/18 14:56
 */
@Bean
public class Config implements Configuration {
    @Bean
    private MapperFactoryBean mapperFactoryBean() {
        return new MapperFactoryBean(UserDao.class);
    }
}
