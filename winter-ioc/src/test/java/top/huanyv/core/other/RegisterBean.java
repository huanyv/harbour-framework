package top.huanyv.core.other;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.core.BeanRegistry;

/**
 * @author admin
 * @date 2022/7/21 16:03
 */
//@Component
public class RegisterBean implements BeanRegistry {


    @Override
    public void set(ApplicationContext applicationContext) {
        applicationContext.register("string", new String("abc"));
    }
}
