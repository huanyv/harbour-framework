package top.huanyv.ioc.core;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author admin
 * @date 2022/7/24 15:06
 */
public interface ApplicationContext extends BeanFactory{

    void register(Object o);

    void register(String beanName, Object o);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    Set<Object> getBeansByAnnotation(Class<? extends Annotation> aClass);

}
