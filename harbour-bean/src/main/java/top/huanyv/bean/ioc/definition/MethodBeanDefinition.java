package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.utils.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/2 20:02
 */
public class MethodBeanDefinition extends AbstractBeanDefinition {

    private Object methodClassInstance;

    private Method method;

    public MethodBeanDefinition(Object methodClassInstance, Method method) {
        Assert.notNull(methodClassInstance, "'methodClassInstance' must not be null.");
        Assert.notNull(method, "'method' must not be null.");
        this.methodClassInstance = methodClassInstance;
        this.method = method;

        setBeanClass(method.getReturnType());
        setBeanName(method.getName());
        setSingleton(method.isAnnotationPresent(Bean.class) && !method.getAnnotation(Bean.class).prototype());
        setLazy(method.isAnnotationPresent(Bean.class) && method.getAnnotation(Bean.class).lazy());
    }

    @Override
    public Object newInstance() {
        try {
            method.setAccessible(true);
            return method.invoke(methodClassInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
