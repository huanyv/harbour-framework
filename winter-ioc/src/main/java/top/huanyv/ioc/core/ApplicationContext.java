package top.huanyv.ioc.core;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author admin
 * @date 2022/7/24 15:06
 */
public interface ApplicationContext extends BeanFactory{

    void registerBean(Object o);

    void registerBean(String beanName, Object o);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    BeanDefinition getBeanDefinition(String beanName);

}
