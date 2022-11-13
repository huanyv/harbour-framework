package top.huanyv.core.test.config;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextWeave;

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
