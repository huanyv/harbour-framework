package top.huanyv.bean.ioc;

import top.huanyv.bean.ioc.definition.BeanDefinition;

/**
 * @author huanyv
 * @date 2022/7/24 15:06
 */
public interface ApplicationContext extends BeanFactory, Configurable {

    void refresh();

    void register(Class<?>... componentClass);

    void registerBean(Class<?> beanClass, Object... constructorArgs);

    void registerBean(String beanName, Class<?> beanClass, Object... constructorArgs);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    BeanDefinition getBeanDefinition(String beanName);
}
