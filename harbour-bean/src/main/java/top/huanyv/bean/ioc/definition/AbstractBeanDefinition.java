package top.huanyv.bean.ioc.definition;

/**
 * @author huanyv
 * @date 2022/12/22 16:08
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    private String beanName;

    private Class<?> beanClass;

    private String scope;

    private boolean lazy;

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    @Override
    public boolean isLazy() {
        return this.lazy;
    }
}
