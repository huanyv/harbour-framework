package top.huanyv.core.config;

import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Configuration;

/**
 * @author admin
 * @date 2022/7/22 15:35
 */
@Configuration
public class CustomConfig {


    @Bean
    public String string2() {
        return "abcdef";
    }



}
