package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Lazy;
import top.huanyv.bean.annotation.Prototype;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.StringUtil;

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
        setBeanName(StringUtil.firstLetterLower(getBeanClass().getSimpleName()));
        setSingleton(!method.isAnnotationPresent(Prototype.class));
        setLazy(method.isAnnotationPresent(Lazy.class));
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
