package top.huanyv.ioc.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import top.huanyv.ioc.core.BeanDefinitionRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/5 9:29
 */
public class CglibInvocationHandler<T> implements MethodInterceptor {

    private T target;

    private AopContext aopContext;

    public CglibInvocationHandler(T target, AopContext aopContext) {
        this.target = target;
        this.aopContext = aopContext;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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

    public CglibInvocationHandler(T target) {
        this.target = target;
    }

    public CglibInvocationHandler(T target, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.target = target;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
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
