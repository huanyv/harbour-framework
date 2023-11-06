package top.huanyv.bean.utils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/8/7 17:17
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * 单例对象缓存
     */
    private static final Map<Class<?>, Object> OBJECT_CACHE = new ConcurrentHashMap<>();

    /**
     * Class对象中的Field属性缓存
     */
    private static final Map<Class<?>, Field[]> fieldsMappingCache = new ConcurrentHashMap<>();

    /**
     * Class对象中的Method属性缓存
     */
    private static final Map<Class<?>, Method[]> methodsMappingCache = new ConcurrentHashMap<>();

    public static Exception getTargetException(InvocationTargetException exception) {
        Throwable targetException = exception.getTargetException();
        if (!InvocationTargetException.class.equals(targetException.getClass())) {
            return (Exception) targetException;
        }
        return getTargetException((InvocationTargetException) targetException);
    }

    /**
     * 根据Class对象创建新实例
     *
     * @param cls cls
     * @return {@link T}
     */
    public static <T> T newInstance(Class<T> cls) {
        try {
            Constructor<T> constructor = cls.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得实例，单实例
     *
     * @param cls cls
     * @return {@link T}
     */
    public static <T> T getInstance(Class<T> cls) {
        if (cls == null) {
            return null;
        }
        Object obj = OBJECT_CACHE.get(cls);
        if (obj == null) {
            obj = ReflectUtil.newInstance(cls);
            OBJECT_CACHE.put(cls, obj);
        }
        return (T) obj;
    }

    public static boolean hasField(Class<?> type, String fieldName) {
        try {
            type.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static void setField(Field field, Object target, Object val) {
        try {
            field.setAccessible(true);
            field.set(target, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据名称获取一个类及其父类的实例属性
     *
     * @param cls  Class对象
     * @param name 属性名称
     * @return {@link Field}
     * @throws NoSuchFieldException 找不到属性异常
     */
    public static Field getAllDeclaredField(Class<?> cls, String name) throws NoSuchFieldException {
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = cls.getSuperclass();
            if (!isObjectClass(superclass)) {
                return getAllDeclaredField(superclass, name);
            }
        }
        throw new NoSuchFieldException(name);
    }

    /**
     * 获取一个类及其父类的所有实例属性
     *
     * @param cls Class对象
     * @return {@link Field[]}
     */
    public static Field[] getAllDeclaredFields(Class<?> cls) {
        Assert.notNull(cls, "'cls' must not be null.");
        Field[] fields = fieldsMappingCache.get(cls);
        if (fields != null) {
            return fields;
        }
        List<Field> result = new ArrayList<>();
        Class<?> c = cls;
        while (!isObjectClass(c)) {
            for (Field field : c.getDeclaredFields()) {
                if (!isStatic(field)) {
                    result.add(field);
                }
            }
            c = c.getSuperclass();
        }
        fields = result.toArray(new Field[0]);
        fieldsMappingCache.put(cls, fields);
        return fields;
    }

    public static Method[] getAllDeclaredMethods(Class<?> cls) {
        Assert.notNull(cls, "'cls' must not be null.");
        Method[] methods = methodsMappingCache.get(cls);
        if (methods != null) {
            return methods;
        }
        List<Method> result = new ArrayList<>();
        Class<?> c = cls;
        while (!isObjectClass(c)) {
            for (Method method : c.getDeclaredMethods()) {
                if (!isStatic(method)) {
                    result.add(method);
                }
            }
            c = c.getSuperclass();
        }
        methods = result.toArray(new Method[0]);
        methodsMappingCache.put(cls, methods);
        return methods;
    }

    public static Object invokeMethod(Method method, Object obj) {
        return invokeMethod(method, obj, new Object[0]);
    }

    public static Object invokeMethod(Method method, Object obj, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("'" + method.getName() + "' method execution failed.");
    }

    public static boolean isGetter(Method method) {
        if (method.getParameterCount() != 0) {
            return false;
        }
        Class<?> returnType = method.getReturnType();
        if (void.class.equals(returnType)) {
            return false;
        }
        String methodName = method.getName();
        if (boolean.class.equals(returnType)) {
            if (methodName.startsWith("is")) {
                return true;
            }
        }
        return methodName.startsWith("get");
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1 && void.class.equals(method.getReturnType());
    }

    public static Method getGetter(Class<?> cls, Field field) throws NoSuchMethodException {
        if (boolean.class.equals(field.getType())) {
            return cls.getMethod("is" + StringUtil.firstLetterUpper(field.getName()));
        }
        return cls.getMethod("get" + StringUtil.firstLetterUpper(field.getName()));
    }

    public static Method getSetter(Class<?> cls, Field field) throws NoSuchMethodException {
        return cls.getMethod("set" + StringUtil.firstLetterUpper(field.getName()), field.getType());
    }

    public static boolean isObjectClass(Class<?> cls) {
        return Object.class.equals(cls);
    }

    public static boolean isObjectMethod(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        if (Object.class.equals(method.getDeclaringClass())) {
            return true;
        }
        if (isToString(method) || isEquals(method) || isClone(method) || isHashCode(method) || isFinalize(method)) {
            return true;
        }
        return false;
    }

    public static boolean isHashCode(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        return "hashCode".equals(method.getName())
                && method.getParameterCount() == 0
                && int.class.equals(method.getReturnType());
    }

    public static boolean isToString(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        return "toString".equals(method.getName())
                && method.getParameterCount() == 0
                && String.class.equals(method.getReturnType());
    }

    public static boolean isEquals(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        return "equals".equals(method.getName())
                && method.getParameterCount() == 1
                && Object.class.equals(method.getParameterTypes()[0])
                && boolean.class.equals(method.getReturnType());
    }

    public static boolean isClone(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        return "clone".equals(method.getName())
                && method.getParameterCount() == 0
                && Object.class.equals(method.getReturnType());
    }

    public static boolean isFinalize(Method method) {
        Assert.notNull(method, "'method' must not be null.");
        return "finalize".equals(method.getName())
                && method.getParameterCount() == 0
                && void.class.equals(method.getReturnType());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }

    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isAbstract(Member member) {
        return Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

}
