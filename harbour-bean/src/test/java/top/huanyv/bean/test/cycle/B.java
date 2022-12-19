package top.huanyv.bean.test.cycle;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.annotation.Scope;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.test.aop.LogAspect;

/**
 * @author huanyv
 * @date 2022/12/19 15:38
 */
@Component
@Aop(LogAspect.class)
public class B {

    @Inject
    private A a;

}
