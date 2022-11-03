package top.huanyv.ioc.core.definition;

import top.huanyv.ioc.anno.Scope;
import top.huanyv.utils.ReflectUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/2 20:02
 */
public class MethodBeanDefinition implements BeanDefinition{

    private String beanName;

    private Class<?> beanClass;

    private String scope;

    private Object methodClassInstance;

    private Method method;

    public MethodBeanDefinition(Object methodClassInstance, Method method) {
        this.methodClassInstance = methodClassInstance;
        this.method = method;

        this.beanClass = method.getReturnType();
        this.beanName = StringUtil.firstLetterLower(this.beanClass.getSimpleName());
        Scope scope = method.getAnnotation(Scope.class);
        this.scope = scope != null ? scope.value() : BeanDefinition.SCOPE_SINGLETON;
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
            return method.invoke(methodClassInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
