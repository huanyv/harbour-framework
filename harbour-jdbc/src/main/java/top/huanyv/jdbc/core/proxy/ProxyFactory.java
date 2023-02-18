package top.huanyv.jdbc.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import top.huanyv.bean.aop.CglibInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author admin
 * @date 2022/7/21 16:59
 */
public class ProxyFactory {

    public static <T> T getImpl(Class<T> clazz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }

    public static <T> T getProxy(Class<T> cls, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), invocationHandler);
    }

    public static <T> T getProxy(Class<T> cls, MethodInterceptor methodInterceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }

}
