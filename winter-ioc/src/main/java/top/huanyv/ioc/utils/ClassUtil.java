package top.huanyv.ioc.utils;


import top.huanyv.ioc.anno.Configuration;

import javax.naming.NameNotFoundException;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassUtil {

    public static Set<Class<?>> getClasses(String pack) {
        return new ClassScanner(pack).scan();
    }

    public static Set<Class<?>> getClassesByAnnotation(String pack, Class<? extends Annotation> annotation) {
        return getClasses(pack).stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

}

