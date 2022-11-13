package top.huanyv.ioc.core.definition;

import top.huanyv.ioc.core.FactoryBean;
import top.huanyv.tools.utils.StringUtil;

/**
 * @author huanyv
 * @date 2022/11/2 20:24
 */
public class FactoryBeanDefinition implements BeanDefinition {

    private String beanName;

    private Class<?> beanClass;

    private String scope;

    private FactoryBean<?> factoryInstance;

    public FactoryBeanDefinition(FactoryBean<?> factoryBeanInstance) {
        this.factoryInstance = factoryBeanInstance;
        this.beanName = StringUtil.firstLetterLower(this.factoryInstance.getClass().getSimpleName());
        this.beanClass = this.factoryInstance.getObjectType();
        this.scope = this.factoryInstance.isSingleton() ? BeanDefinition.SCOPE_SINGLETON : BeanDefinition.SCOPE_PROTOTYPE;
    }

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
    public Object newInstance() {
        try {
            return this.factoryInstance.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
