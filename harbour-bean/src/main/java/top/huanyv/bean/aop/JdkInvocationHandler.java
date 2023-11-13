package top.huanyv.bean.aop;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/8/5 9:29
 */
public class JdkInvocationHandler<T> extends AbstractInvocationHandler<T> implements InvocationHandler {

    private T target;

    private AopContext aopContext;

    public JdkInvocationHandler(T target, AopContext aopContext) {
        super(target, aopContext);
        this.target = target;
        this.aopContext = aopContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return handle(proxy, method, args);
    }

}
