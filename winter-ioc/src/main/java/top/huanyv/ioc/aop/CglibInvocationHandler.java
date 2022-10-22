package top.huanyv.ioc.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/8/5 9:29
 */
public class CglibInvocationHandler<T> extends AbstractInvocationHandler<T> implements MethodInterceptor {

    private T target;

    private AopContext aopContext;

    public CglibInvocationHandler(T target, AopContext aopContext) {
        super(target, aopContext);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return handle(o, method, args);
    }

}
