package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ObjectFactory;
import top.huanyv.bean.utils.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/2 20:02
 */
public class MethodBeanDefinition extends AbstractBeanDefinition {

    private Method method;

    private ObjectFactory<?> objectFactory;

    public MethodBeanDefinition(ObjectFactory<?> objectFactory, Method method) {
        Assert.notNull(objectFactory, "'objectFactory' must not be null.");
        Assert.notNull(method, "'method' must not be null.");
        this.objectFactory = objectFactory;
        this.method = method;

        Bean beanAnnotation = method.getAnnotation(Bean.class);
        setBeanClass(method.getReturnType());
        setBeanName(method.getName());
        setSingleton(beanAnnotation != null && !beanAnnotation.prototype());
        setLazy(beanAnnotation != null && beanAnnotation.lazy());
    }

    @Override
    public Object newInstance() {
        try {
            method.setAccessible(true);
            return method.invoke(this.objectFactory.getObject());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
