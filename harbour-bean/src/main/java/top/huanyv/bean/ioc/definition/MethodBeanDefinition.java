package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Lazy;
import top.huanyv.bean.annotation.Scope;
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
        this.methodClassInstance = methodClassInstance;
        this.method = method;

        setBeanClass(method.getReturnType());
        setBeanName(method.getName());
        Scope scope = method.getAnnotation(Scope.class);
        setScope(scope != null ? scope.value() : BeanDefinition.SCOPE_SINGLETON);
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
