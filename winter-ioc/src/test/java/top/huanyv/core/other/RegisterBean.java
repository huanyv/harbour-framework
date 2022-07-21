package top.huanyv.core.other;

import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.processRegisterBean;

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
