package top.huanyv.bean.utils;

import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author admin
 * @date 2022/7/28 14:50
 */
public class BeanFactoryUtil {

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

    public static List<Object> getBeansByAnnotation(ApplicationContext applicationContext, Class<? extends Annotation> clazz) {
        List<Object> beanList = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            if (bean.getClass().isAnnotationPresent(clazz)) {
                beanList.add(bean);
            }
        }
        return beanList;
    }

    public static <T> List<T> getBeansByType(ApplicationContext applicationContext, Class<T> clazz) {
        List<T> beanList = new ArrayList<>();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            if (clazz.isInstance(bean)) {
                beanList.add((T) bean);
            }
        }
        return beanList;
    }

    /**
     * bean存在
     *
     * @param applicationContext 应用程序上下文
     * @param type               类型
     * @return boolean
     */
    public static boolean isPresent(ApplicationContext applicationContext, Class<?> type) {
        try {
            applicationContext.getBean(type);
            return true;
        } catch (NoSuchBeanDefinitionException e) {
            return false;
        }
    }

    /**
     * bean不存在
     *
     * @param applicationContext 应用程序上下文
     * @param type               类型
     * @return boolean
     */
    public static boolean isNotPresent(ApplicationContext applicationContext, Class<?> type) {
        return !isPresent(applicationContext, type);
    }

}
