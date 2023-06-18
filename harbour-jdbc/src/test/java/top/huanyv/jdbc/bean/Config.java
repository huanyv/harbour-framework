package top.huanyv.jdbc.bean;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.jdbc.support.DaoScanner;

/**
 * @author huanyv
 * @date 2023/5/15 13:53
 */
@Configuration
public class Config {

    @Bean
    public DaoScanner daoScanner() {
        return new DaoScanner("top.huanyv.jdbc");
    }

}
