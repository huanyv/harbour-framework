package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Lazy;
import top.huanyv.bean.annotation.Scope;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author huanyv
 * @date 2022/11/2 19:48
 */
public class ClassBeanDefinition implements BeanDefinition {

    private String beanName;

    private Class<?> beanClass;

    private String scope;

    private boolean lazy;

    private Object[] constructorArgs;

    private Constructor<?> constructor;

    public ClassBeanDefinition(Class<?> beanClass, Object... constructorArgs) {
        this.beanClass = beanClass;
        this.beanName = StringUtil.firstLetterLower(this.beanClass.getSimpleName());
        Scope scope = beanClass.getAnnotation(Scope.class);
        this.scope = scope != null ? scope.value() : BeanDefinition.SCOPE_SINGLETON;
        this.lazy = beanClass.isAnnotationPresent(Lazy.class);

        this.constructorArgs = constructorArgs;
        handleConstructor();
    }

    private void handleConstructor() {
        try {
            // 如果没有构造参数，为无参构造
            if (this.constructorArgs.length == 0) {
                this.constructor = this.beanClass.getDeclaredConstructor();
                return;
            }
            for (Constructor<?> constructor : this.beanClass.getConstructors()) {
                if (isConstructor(constructor, constructorArgs)) {
                    this.constructor = constructor;
                }
            }
            if (this.constructor == null) {
                throw new IllegalArgumentException("No constructor for '" + Arrays.toString(constructorArgs) + "'.");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private boolean isConstructor(Constructor<?> constructor, Object[] constructorArgs) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length != constructorArgs.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (!parameterType.isInstance(constructorArgs[i])) {
                return false;
            }
        }
        return true;
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
    public boolean isLazy() {
        return this.lazy;
    }

    @Override
    public Object newInstance() {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(constructorArgs);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
