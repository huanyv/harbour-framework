package top.huanyv.bean.test.aop;

import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.bean.aop.JoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * @author huanyv
 * @date 2022/11/18 14:40
 */
public class LogAspect2 implements AspectAdvice {
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
        System.out.println(this.getClass() + "环绕通知1");
        Object result = point.invoke();
        System.out.println(this.getClass() + "环绕通知2");
        return result;
    }
}
