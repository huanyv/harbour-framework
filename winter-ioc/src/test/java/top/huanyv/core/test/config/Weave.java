package top.huanyv.core.test.config;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.core.ApplicationContextWeave;

/**
 * @author huanyv
 * @date 2022/10/20 22:05
 */
public class Weave implements ApplicationContextWeave {
    @Override
    public void populateBeanBefore(ApplicationContext applicationContext) {
        System.out.println("weave....");
    }
}
