package top.huanyv.bean.test.factory;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.BeanDefinitionRegistry;
import top.huanyv.bean.ioc.BeanDefinitionRegistryPostProcessor;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.bean.test.ioc.controller.UserController;

/**
 * @author huanyv
 * @date 2023/5/10 15:41
 */
@Bean
public class BeanDefProcess implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("registry = " + registry);
        System.out.println("Bean定义后，注册容器回调");
        ClassBeanDefinition classBeanDefinition = new ClassBeanDefinition(MapperFactoryBean.class, BookDao.class);
        registry.register("bookDao", classBeanDefinition);

        ClassBeanDefinition beanDefinition = new ClassBeanDefinition(String.class);
        registry.register(beanDefinition);

        for (BeanDefinition definition : registry) {
            System.out.println("definition = " + definition);
        }
    }
}
