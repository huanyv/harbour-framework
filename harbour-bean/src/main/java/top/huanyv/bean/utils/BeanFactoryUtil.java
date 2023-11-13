package top.huanyv.bean.utils;

import top.huanyv.bean.exception.BeanTypeNonUniqueException;
import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.definition.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author huanyv
 * @date 2022/7/28 14:50
 */
public final class BeanFactoryUtil {

    private BeanFactoryUtil() {
    }

    public static Map<String, Object> getBeanMap(ApplicationContext applicationContext) {
        Map<String, Object> beanMap = new HashMap<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            beanMap.put(beanName, bean);
        }
        return beanMap;
    }

    public static List<Object> getBeans(ApplicationContext applicationContext) {
        List<Object> beanList = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            beanList.add(bean);
        }
        return beanList;
    }

    public static List<Object> getBeansByAnnotation(ApplicationContext applicationContext, Class<? extends Annotation> annotation) {
        List<Object> beanList = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> beanClass = applicationContext.getBeanDefinition(beanName).getBeanClass();
            if (beanClass.isAnnotationPresent(annotation)) {
                beanList.add(applicationContext.getBean(beanName));
            }
        }
        return beanList;
    }

    public static List<Class<?>> getBeanClasses(ApplicationContext applicationContext) {
        List<Class<?>> classes = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            classes.add(applicationContext.getBeanDefinition(beanName).getBeanClass());
        }
        return classes;
    }

    public static <T> List<T> getBeansByType(ApplicationContext applicationContext, Class<T> cls) {
        List<T> beanList = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> beanClass = applicationContext.getBeanDefinition(beanName).getBeanClass();
            if (cls.isAssignableFrom(beanClass)) {
                beanList.add((T) applicationContext.getBean(beanName));
            }
        }
        return beanList;
    }

    /**
     * 某个类型的Bean是否存在
     *
     * @param applicationContext 应用程序上下文
     * @param type               类型
     * @return boolean
     */
    public static boolean isPresent(ApplicationContext applicationContext, Class<?> type) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = applicationContext.getBeanDefinition(beanName);
            if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 某个类型的Bean是否不存在
     *
     * @param applicationContext 应用程序上下文
     * @param type               类型
     * @return boolean
     */
    public static boolean isNotPresent(ApplicationContext applicationContext, Class<?> type) {
        return !isPresent(applicationContext, type);
    }

}
