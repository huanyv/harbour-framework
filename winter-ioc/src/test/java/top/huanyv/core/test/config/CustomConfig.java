package top.huanyv.core.test.config;

import top.huanyv.core.test.controller.UserController;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.ioc.core.ApplicationContextWeave;

/**
 * @author huanyv
 * @date 2022/10/20 22:04
 */
@Configuration
public class CustomConfig {

    @Bean
    public ApplicationContextWeave weave() {
        return new Weave();
    }

    @Bean
    public UserController userController() {
        return new UserController();
    }

}
