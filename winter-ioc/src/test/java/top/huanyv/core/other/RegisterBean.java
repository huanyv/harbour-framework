package top.huanyv.core.other;

import top.huanyv.anno.Component;
import top.huanyv.core.AnnotationConfigApplicationContext;
import top.huanyv.core.BeanDefinition;
import top.huanyv.core.BeanFactory;
import top.huanyv.core.processRegisterBean;
import top.huanyv.core.service.UserService;

import java.util.List;

/**
 * @author admin
 * @date 2022/7/21 16:03
 */
@Component
public class RegisterBean implements processRegisterBean {

    @Override
    public void registerBean(AnnotationConfigApplicationContext applicationContext) {
        applicationContext.register("string", new String("abc"));
    }
}
