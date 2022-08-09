package top.huanyv.ioc.core;


import top.huanyv.ioc.aop.Aop;

import java.lang.reflect.Method;

public class BeanDefinition {
    private String beanName;
    private Class<?> beanClass;

    public BeanDefinition() {
    }

    public BeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isNeedProxy() {
        Class<?> beanClass = this.getBeanClass();
        if (beanClass.isAnnotationPresent(Aop.class)) {
            return true;
        }
        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Aop.class)) {
                return true;
            }
        }
        return false;
    }


}
