package top.huanyv.ioc.core.definition;

import top.huanyv.ioc.anno.Scope;
import top.huanyv.utils.ReflectUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/2 19:48
 */
public class ClassBeanDefinition implements BeanDefinition {

    private String beanName;

    private Class<?> beanClass;

    private String scope;

    private Object[] constructorArgs;

    public ClassBeanDefinition(Class<?> beanClass, Object... constructorArgs) {
        this.beanClass = beanClass;
        this.constructorArgs = constructorArgs;
        this.beanName = StringUtil.firstLetterLower(this.beanClass.getSimpleName());
        Scope scope = beanClass.getAnnotation(Scope.class);
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
            Class<?>[] parameterTypes = new Class[constructorArgs.length];
            for (int i = 0; i < constructorArgs.length; i++) {
                parameterTypes[i] = constructorArgs[i].getClass();
            }
            Constructor<?> constructor = this.beanClass.getConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(constructorArgs);
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
