package top.huanyv.ioc.core;

import top.huanyv.ioc.core.definition.BeanDefinition;

/**
 * @author admin
 * @date 2022/7/24 15:06
 */
public interface ApplicationContext extends BeanFactory {

    void refresh();

    void register(Class<?>... componentClass);

    void register(Class<?> beanClass, Object... constructorArgs);

    void register(String beanName, Class<?> beanClass, Object... constructorArgs);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    BeanDefinition getBeanDefinition(String beanName);
}
