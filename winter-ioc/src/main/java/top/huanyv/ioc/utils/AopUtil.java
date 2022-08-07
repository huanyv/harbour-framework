package top.huanyv.ioc.utils;

import top.huanyv.utils.ProxyUtil;

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
        if (ProxyUtil.isJdkProxy(proxy)) {
            proxy = getJdkTargetObject(proxy);
        } else if (ProxyUtil.isCglibPoxy(proxy)) {
            proxy = getCglibTargetObject(proxy);
        }

        return getTargetObject(proxy);
    }

    public static Object getJdkTargetObject(Object proxy) {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("target");
            field.setAccessible(true);
            return field.get(invocationHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCglibTargetObject(Object proxy) {
        try {
            Field field = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            field.setAccessible(true);
            Object invokeHandler = field.get(proxy);
            Field targetField = invokeHandler.getClass().getDeclaredField("target");
            targetField.setAccessible(true);
            return targetField.get(invokeHandler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
