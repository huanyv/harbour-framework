package top.huanyv.core.aop;

import top.huanyv.ioc.aop.AspectAdvice;
import top.huanyv.ioc.aop.JoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * @author admin
 * @date 2022/8/5 8:52
 */
public class Test02Aop implements AspectAdvice {
    @Override
    public void beforeAdvice(Object[] args) {
        System.out.println(this.getClass().getSimpleName() + "before");
    }

    @Override
    public void afterAdvice(Object[] args, Object result) {
        System.out.println(this.getClass().getSimpleName() + "after");
    }

    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        System.out.println(this.getClass() + "around");
        Object result = point.invoke();
        System.out.println(this.getClass() + "around");
        return result;
    }
}
