package top.huanyv.ioc.utils;


import java.util.Set;

public class ClassUtil {

    public static Set<Class<?>> getClasses(String pack) {
        return new ClassScanner(pack).scan();
    }

}

