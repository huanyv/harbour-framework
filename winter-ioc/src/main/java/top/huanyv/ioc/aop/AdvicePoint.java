package top.huanyv.ioc.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/8/5 15:58
 */
public class AdvicePoint {

    private Object target;

    private Method method;

    private Object[] args;

    private AspectAdvice aspect;


    public Object invoke() throws InvocationTargetException, IllegalAccessException {

        aspect.beforeAdvice(args);
        Object result = method.invoke(target, args);
        aspect.afterAdvice(args, method);

        return result;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setAspect(AspectAdvice aspect) {
        this.aspect = aspect;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
