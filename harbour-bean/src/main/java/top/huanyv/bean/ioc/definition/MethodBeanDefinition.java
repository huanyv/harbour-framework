package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Lazy;
import top.huanyv.bean.annotation.Prototype;
import top.huanyv.tools.utils.Assert;

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
        Assert.notNull(method, "'methid' must not be null.");
        this.methodClassInstance = methodClassInstance;
        this.method = method;

        setBeanClass(method.getReturnType());
        setBeanName(method.getName());
        setSingleton(!method.isAnnotationPresent(Prototype.class));
        setLazy(method.isAnnotationPresent(Lazy.class));
    }

    @Override
    public Object newInstance() {
        try {
            return method.invoke(methodClassInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
