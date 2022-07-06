package top.huanyv.config;

import top.huanyv.annotation.Configuration;

/**
 * @author admin
 * @date 2022/7/6 11:03
 */
@Configuration
public class WebConfigurationImpl implements WebConfiguration {
    @Override
    public int getServerPort() {
        return 9090;
    }
}
