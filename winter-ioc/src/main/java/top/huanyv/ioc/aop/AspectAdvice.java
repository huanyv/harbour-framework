package top.huanyv.ioc.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author admin
 * @date 2022/8/5 8:44
 */
public interface AspectAdvice {

    default void beforeAdvice(Object[] args) {

    }

    default void afterAdvice(Object[] args, Object result) {

    }

    default Object aroundAdvice(AdvicePoint point) throws InvocationTargetException, IllegalAccessException {
        return point.invoke();
    }

}
