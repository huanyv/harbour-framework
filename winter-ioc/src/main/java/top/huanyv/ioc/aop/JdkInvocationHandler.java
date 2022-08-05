package top.huanyv.ioc.aop;

import jdk.nashorn.internal.ir.IfNode;
import top.huanyv.ioc.core.BeanDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/8/5 9:29
 */
public class JdkInvocationHandler<T> implements InvocationHandler {

    private T target;


    public JdkInvocationHandler(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        boolean isProxy = isProxy(method.getName());
        if (isProxy) {
            BaseAop aop = getAopInstance(method.getName());

            AdvicePoint advicePoint = new AdvicePoint();
            advicePoint.setTarget(target);
            advicePoint.setMethod(method);
            advicePoint.setArgs(args);
            advicePoint.setAspect(aop);

            result = aop.aroundAdvice(advicePoint);
        } else {
            result = method.invoke(target, args);
        }

        return result;
    }

    public boolean isProxy(String methodName) {
        if (target.getClass().isAnnotationPresent(Aop.class)) {
            return true;
        }
        try {
            if (target.getClass().getMethod(methodName).isAnnotationPresent(Aop.class)) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            return false;
        }
        return false;
    }


    public BaseAop getAopInstance(String methodName) {
        Aop aop = target.getClass().getAnnotation(Aop.class);
        try {
            if (aop != null) {
                return aop.value().getConstructor().newInstance();
            }
            aop = target.getClass().getMethod(methodName).getAnnotation(Aop.class);
            if (aop != null) {
                return aop.value().getConstructor().newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
