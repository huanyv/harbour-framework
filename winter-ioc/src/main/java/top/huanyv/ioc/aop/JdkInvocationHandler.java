package top.huanyv.ioc.aop;

import top.huanyv.ioc.core.BeanDefinitionRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2022/8/5 9:29
 */
public class JdkInvocationHandler<T> implements InvocationHandler {

    private T target;

    private AopContext aopContext;

    public JdkInvocationHandler(T target, AopContext aopContext) {
        this.target = target;
        this.aopContext = aopContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (aopContext.hasProxy(targetMethod)) {
            // 获取当前方法的切面链
            List<AspectAdvice> advices = aopContext.getAspectAdvice(targetMethod);

            JoinPoint joinPoint = new JoinPoint();
            joinPoint.setTarget(target);
            joinPoint.setMethod(method);
            joinPoint.setArgs(args);
            joinPoint.setChain(advices);

            result = joinPoint.run();
        } else {
            method.setAccessible(true);
            result = method.invoke(target, args);
        }
        return result;
    }
/*
    private T target;

    private BeanDefinitionRegistry beanDefinitionRegistry;


    public JdkInvocationHandler(T target) {
        this.target = target;
    }

    public JdkInvocationHandler(T target, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.target = target;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        boolean isProxy = beanDefinitionRegistry.isNeedProxy(target.getClass(), method);
        if (isProxy) {
            Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            AspectAdvice aop = beanDefinitionRegistry.getBeanAspect(targetMethod);

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

    public boolean isProxy(Method method) {
        if (target.getClass().isAnnotationPresent(Aop.class)) {
            return true;
        }
        try {
            if (target.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Aop.class)) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            return false;
        }
        return false;
    }


    public AspectAdvice getAopInstance(Method method) {
        Aop aop = target.getClass().getAnnotation(Aop.class);
        try {
            if (aop != null) {
                return aop.value().getConstructor().newInstance();
            }
            aop = target.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(Aop.class);
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
*/



}
