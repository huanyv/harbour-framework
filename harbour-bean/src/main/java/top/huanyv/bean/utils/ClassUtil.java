package top.huanyv.bean.utils;

import top.huanyv.bean.utils.ClassScanner;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2022/7/15 15:59
 */
public class ClassUtil {

    /**
     * 从包下获取所有class对象
     *
     * @param packs 包名
     * @return 对象set集合
     */
    public static Set<Class<?>> getClasses(String... packs) {
        Set<Class<?>> classes = new HashSet<>();
        if (packs == null) {
            return classes;
        }
        for (String pack : packs) {
            classes.addAll(new ClassScanner(pack.trim()).scan());
        }
        return classes;
    }

    /**
     * 从包下获取所有，指定注解的class对象
     *
     * @param scanPackages 包名
     * @param annotation 注解class
     * @return class对象集合
     */
    public static Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation, String... scanPackages) {
        return getClasses(scanPackages).stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    /**
     * 从指定包下，获取指定类型的class对象列表
     *
     * @param type        类型
     * @param scanPackages 扫描包
     * @return {@link Set}<{@link Class}<{@link ?}>>
     */
    public static Set<Class<?>> getClassesByType(Class<?> type, String... scanPackages) {
        return getClasses(scanPackages).stream()
                .filter(cls -> type.isAssignableFrom(cls))
                .collect(Collectors.toSet());
    }

    /**
     * 一个类是否存在
     *
     * @param className 类名
     * @return boolean
     */
    public static boolean isPresent(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 是否常见值类型
     *
     * @param clazz clazz
     * @return boolean
     */
    public static boolean isCommonType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isPrimitive() // 基本数据类型
                || Number.class.isAssignableFrom(clazz) // 数字包装类型
                || CharSequence.class.isAssignableFrom(clazz); // 各种字符类型
    }


    /**
     * 是否为数字类型
     *
     * @param cls cls
     * @return boolean
     */
    public static boolean isNumberType(Class<?> cls) {
        return  Number.class.isAssignableFrom(cls)
                || short.class.equals(cls) || int.class.equals(cls) || byte.class.equals(cls)
                || long.class.equals(cls) || float.class.equals(cls) || double.class.equals(cls);
    }
}
