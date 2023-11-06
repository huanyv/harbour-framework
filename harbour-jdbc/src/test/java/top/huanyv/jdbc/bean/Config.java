package top.huanyv.jdbc.bean;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.jdbc.support.DaoScanner;

/**
 * @author huanyv
 * @date 2023/5/15 13:53
 */
@Bean
public class Config implements Configuration {

    @Bean
    public DaoScanner daoScanner() {
        return new DaoScanner("top.huanyv.jdbc");
    }

}
