package top.huanyv.bean.test.ioc.aware;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.exception.BeansException;
import top.huanyv.bean.ioc.BeanPostProcessor;

/**
 * @author huanyv
 * @date 2023/11/14 14:03
 */
@Bean
public class BeanPost implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化之前............");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("初始化之后............");
        return bean;
    }
}
