package top.huanyv.bean.aop;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/22 16:09
 */
public abstract class AbstractInvocationHandler<T> {

    protected T target;

    protected AopContext aopContext;

    public AbstractInvocationHandler(T target, AopContext aopContext) {
        this.target = target;
        this.aopContext = aopContext;
    }

    public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        targetMethod.setAccessible(true);
        if (aopContext.hasProxy(targetMethod)) {
            // 获取当前方法的切面链
            List<AspectAdvice> advices = aopContext.getAspectAdvice(targetMethod);

            // 创建切点
            JoinPoint joinPoint = new JoinPoint();
            joinPoint.setTarget(target);
            joinPoint.setMethod(targetMethod);
            joinPoint.setArgs(args);
            joinPoint.setChain(advices); // 执行链

            result = joinPoint.run(); // 执行
        } else {
            result = targetMethod.invoke(target, args);
        }
        return result;
    }

}
