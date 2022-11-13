package top.huanyv.core.test.config;

import top.huanyv.core.test.controller.UserController;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.bean.ioc.ApplicationContextWeave;

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

//    @Bean
//    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserController userController() {
        return new UserController();
    }

}
