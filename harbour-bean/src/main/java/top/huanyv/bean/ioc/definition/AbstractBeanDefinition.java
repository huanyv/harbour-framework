package top.huanyv.bean.ioc.definition;

/**
 * @author huanyv
 * @date 2022/12/22 16:08
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    private String beanName;

    private Class<?> beanClass;

    private boolean singleton;

    private boolean lazy;

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public boolean isLazy() {
        return lazy;
    }

    @Override
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

}
