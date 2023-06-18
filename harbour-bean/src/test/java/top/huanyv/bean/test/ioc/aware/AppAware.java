package top.huanyv.bean.test.ioc.aware;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextAware;

/**
 * @author huanyv
 * @date 2023/5/10 15:37
 */
@Component
public class AppAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("applicationContext = " + applicationContext);
        System.out.println("回调上下文");
    }
}
