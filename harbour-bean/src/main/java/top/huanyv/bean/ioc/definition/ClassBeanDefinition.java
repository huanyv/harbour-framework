package top.huanyv.bean.ioc.definition;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author huanyv
 * @date 2022/11/2 19:48
 */
public class ClassBeanDefinition extends AbstractBeanDefinition {

    private Object[] constructorArgs;

    private Constructor<?> constructor;

    public ClassBeanDefinition(Class<?> beanClass, Object... constructorArgs) {
        Assert.notNull(beanClass, "'beanClass' must not be null.");
        setBeanClass(beanClass);
        setBeanName(StringUtil.firstLetterLower(beanClass.getSimpleName()));
        setSingleton(beanClass.isAnnotationPresent(Bean.class) && !beanClass.getAnnotation(Bean.class).prototype());
        setLazy(beanClass.isAnnotationPresent(Bean.class) && beanClass.getAnnotation(Bean.class).lazy());

        if (constructorArgs != null) {
            this.constructorArgs = constructorArgs;
            handleConstructor();
        }
    }

    private void handleConstructor() {
        Class<?> beanClass = getBeanClass();
        try {
            // 如果没有构造参数，为无参构造
            if (this.constructorArgs.length == 0) {
                this.constructor = beanClass.getDeclaredConstructor();
                return;
            }
            for (Constructor<?> constructor : beanClass.getConstructors()) {
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
