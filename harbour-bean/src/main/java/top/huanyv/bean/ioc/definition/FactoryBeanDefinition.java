package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.FactoryBean;
import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.StringUtil;

/**
 * @author huanyv
 * @date 2022/11/2 20:24
 */
public class FactoryBeanDefinition extends AbstractBeanDefinition {

    private FactoryBean<?> factoryInstance;

    public FactoryBeanDefinition(FactoryBean<?> factoryBeanInstance) {
        Assert.notNull(factoryBeanInstance, "'factoryBeanInstance' must not be null.");
        this.factoryInstance = factoryBeanInstance;
        Class<? extends FactoryBean> factoryInstanceClass = this.factoryInstance.getClass();
        setBeanName(StringUtil.firstLetterLower(factoryInstanceClass.getSimpleName()));
        setBeanClass(this.factoryInstance.getObjectType());
        setSingleton(this.factoryInstance.isSingleton());
        setLazy(factoryInstanceClass.isAnnotationPresent(Bean.class) && factoryInstanceClass.getAnnotation(Bean.class).lazy());
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
