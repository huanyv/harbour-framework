package top.huanyv.ioc.utils;

import top.huanyv.tools.utils.ProxyUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author huanyv
 * @date 2022/8/7 16:40
 */
public class AopUtil {

    public static Class<?> getTargetClass(Object proxy) {
        return getTargetObject(proxy).getClass();
    }

    public static Object getTargetObject(Object proxy) {
        if (!ProxyUtil.isProxyClass(proxy.getClass())) {
            return proxy;
        }

        Object target = null;
        if (ProxyUtil.isJdkProxy(proxy)) {
            target = getJdkTargetObject(proxy);
        } else if (ProxyUtil.isCglibPoxy(proxy)) {
            target = getCglibTargetObject(proxy);
        }

        // 不是AOP代理类
        if (target == null) {
            return proxy;
        }

        return getTargetObject(target);
    }

    public static Object getJdkTargetObject(Object proxy) {
        // 获取代理回调对象 JdkInvocationHandler<T> implements InvocationHandler
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
        try {
            // 获取源对象属性
            Field targetField = invocationHandler.getClass().getDeclaredField("target");
            targetField.setAccessible(true);
            // 源对象值
            return targetField.get(invocationHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        return null;
    }

    public static Object getCglibTargetObject(Object proxy) {
        try {
            // 获取代理回调对象属性 CglibInvocationHandler<T> implements MethodInterceptor
            Field field = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            field.setAccessible(true);
            // 获取代理回调对象属性值
            Object invokeHandler = field.get(proxy);
            // 获取源对象
            Field targetField = invokeHandler.getClass().getDeclaredField("target");
            targetField.setAccessible(true);
            // 源对象值
            return targetField.get(invokeHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        return null;
    }

}
