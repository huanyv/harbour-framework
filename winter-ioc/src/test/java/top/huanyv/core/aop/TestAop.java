package top.huanyv.core.aop;

import top.huanyv.ioc.aop.AdvicePoint;
import top.huanyv.ioc.aop.BaseAop;

import java.lang.reflect.InvocationTargetException;
import java.net.PortUnreachableException;

/**
 * @author admin
 * @date 2022/8/5 8:52
 */
public class TestAop implements BaseAop {
    @Override
    public void beforeAdvice(Object[] args) {
        System.out.println(this.getClass().getSimpleName() + "before");
    }

    @Override
    public void afterAdvice(Object[] args, Object result) {
        System.out.println(this.getClass().getSimpleName() + "after");
    }


    @Override
    public Object aroundAdvice(AdvicePoint point) throws InvocationTargetException, IllegalAccessException {
        System.out.println("aroud ....");
        Object result = point.invoke();
        System.out.println("aroud ....");
        return result;
    }
}
