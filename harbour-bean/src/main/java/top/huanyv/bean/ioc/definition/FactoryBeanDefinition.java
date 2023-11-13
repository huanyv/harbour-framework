package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.FactoryBean;
import top.huanyv.bean.ioc.ObjectFactory;
import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.ReflectUtil;
import top.huanyv.bean.utils.StringUtil;

/**
 * @author huanyv
 * @date 2022/11/2 20:24
 */
public class FactoryBeanDefinition extends AbstractBeanDefinition {

    private ObjectFactory<? extends FactoryBean<?>> objectFactory;

    public FactoryBeanDefinition(ObjectFactory<? extends FactoryBean<?>> objectFactory, Class<?> factoryBeanClass, FactoryBean<?> tempFactoryBean) {
        Assert.notNull(objectFactory, "'objectFactory' must not be null.");
        Assert.notNull(factoryBeanClass, "'factoryBeanClass' must not be null.");
        Assert.notNull(tempFactoryBean, "'tempFactoryBean' must not be null.");
        this.objectFactory = objectFactory;
        setBeanName(StringUtil.firstLetterLower(factoryBeanClass.getSimpleName()));
        setBeanClass(tempFactoryBean.getObjectType());
        setSingleton(tempFactoryBean.isSingleton());
        setLazy(factoryBeanClass.isAnnotationPresent(Bean.class) && factoryBeanClass.getAnnotation(Bean.class).lazy());
    }

    @Override
    public Object newInstance() {
        try {
            return this.objectFactory.getObject().getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
