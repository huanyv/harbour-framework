package top.huanyv.bean.test.factory;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.test.ioc.dao.UserDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("server.port", "8080");
        return map;
    }
}
