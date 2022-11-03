package top.huanyv.ioc.core.definition;

public interface BeanDefinition {

    public static final String SCOPE_PROTOTYPE = "prototype";

    public static final String SCOPE_SINGLETON = "singleton";

    String getBeanName();

    void setBeanName(String beanName);

    Class<?> getBeanClass();

    void setBeanClass(Class<?> beanClass);

    String getScope();

    void setScope(String scope);

    Object newInstance();

}
