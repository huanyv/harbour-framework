package top.huanyv.bean.test.cycle;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.test.aop.LogAspect;

/**
 * @author huanyv
 * @date 2022/12/19 15:38
 */
@Bean
@Aop(LogAspect.class)
public class A {

    @Inject
    private B b;

}
