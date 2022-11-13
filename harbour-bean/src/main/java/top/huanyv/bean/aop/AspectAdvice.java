package top.huanyv.bean.aop;

import java.lang.reflect.InvocationTargetException;

/**
 * @author admin
 * @date 2022/8/5 8:44
 */
public interface AspectAdvice {

    default void beforeAdvice(Object[] args) {

    }

    default void afterAdvice(Object[] args, Object result) {

    }

    default Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        return point.invoke();
    }

}
