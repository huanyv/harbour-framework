package top.huanyv.core.test.aop;

import top.huanyv.ioc.aop.AspectAdvice;
import top.huanyv.ioc.aop.JoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * @author huanyv
 * @date 2022/10/20 21:06
 */
public class Aop02 implements AspectAdvice {
    @Override
    public void beforeAdvice(Object[] args) {
        System.out.println(this.getClass() + "前置通知");
    }

    @Override
    public void afterAdvice(Object[] args, Object result) {
        System.out.println(this.getClass() + "后置通知");
    }

    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        System.out.println(this.getClass() + "环绕通知之前");
        Object result = point.invoke();
        System.out.println(this.getClass() + "环绕通知之后");
        return result;
    }
}
