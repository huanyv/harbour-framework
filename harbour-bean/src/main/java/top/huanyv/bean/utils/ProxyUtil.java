package top.huanyv.bean.utils;

import java.lang.reflect.Proxy;

/**
 * @author huanyv
 * @date 2022/8/7 15:42
 */
public class ProxyUtil {

    public static boolean isProxyClass(Object o) {
        return isProxyClass(o.getClass());
    }
    public static boolean isProxyClass(Class<?> clazz) {
        return isJdkProxy(clazz) || isCglibPoxy(clazz);
    }

    public static boolean isJdkProxy(Object o) {
        return isJdkProxy(o.getClass());
    }
    public static boolean isJdkProxy(Class<?> clazz) {
        return clazz != null && Proxy.isProxyClass(clazz);
    }

    public static boolean isCglibPoxy(Object o) {
        return isCglibPoxy(o.getClass());
    }
    public static boolean isCglibPoxy(Class<?> clazz) {
        return clazz != null && clazz.getName().contains("$$EnhancerByCGLIB$$");
    }


}
