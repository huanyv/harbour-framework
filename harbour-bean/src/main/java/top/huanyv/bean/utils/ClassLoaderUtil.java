package top.huanyv.bean.utils;

import java.io.InputStream;

/**
 * @author admin
 * @date 2022/7/27 15:01
 */
public class ClassLoaderUtil {

    public static InputStream getInputStream(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
