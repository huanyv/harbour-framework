package top.huanyv.bean.ioc.definition;

public interface BeanDefinition {

    String getBeanName();

    void setBeanName(String beanName);

    Class<?> getBeanClass();

    void setBeanClass(Class<?> beanClass);

    void setSingleton(boolean singleton);

    boolean isSingleton();

    void setLazy(boolean lazy);

    boolean isLazy();

    Object newInstance();

}
