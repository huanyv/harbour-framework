package top.huanyv.bean.aop;

import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.ConcurrentHashSet;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/9/12 15:26
 */
public class AopContext {

    /**
     * aop映射
     */
    private final Map<Method, List<AspectAdvice>> aopMapping = new ConcurrentHashMap<>();

    // 已经添加过代理映射的缓存，防止重复添加
    private final Set<String> adviceCache = new ConcurrentHashSet<>();

    /**
     * 获取切面通知
     *
     * @param method 方法
     * @return {@link List}<{@link AspectAdvice}>
     */
    public List<AspectAdvice> getAspectAdvice(Method method) {
        return aopMapping.get(method);
    }

    /**
     * 判断类和方法是否需要代理
     *
     * @param cls    cls
     * @param method 方法
     * @return boolean
     */
    public boolean hasProxy(Class<?> cls, Method method) {
        try {
            Method targetMethod = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return aopMapping.containsKey(targetMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断某个方法是否需要代理
     *
     * @param method 方法
     * @return boolean 布尔
     */
    public boolean hasProxy(Method method) {
        return aopMapping.containsKey(method);
    }

    /**
     * 判断类是否需要代理
     *
     * @param cls 类
     * @return boolean
     */
    public boolean hasProxy(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            if (aopMapping.containsKey(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加aop映射
     *
     * @param method  方法
     * @param advices 建议
     */
    public AopContext addMapping(Method method, Class<? extends AspectAdvice>... advices) {
        List<AspectAdvice> list = this.aopMapping.get(method);
        if (list == null) {
            // 如果不存在，创建新的
            list = new ArrayList<>();
        }
        for (Class<? extends AspectAdvice> advice : advices) {
            // 创建切面实例
            AspectAdvice aspectAdvice = ReflectUtil.newInstance(advice);
            if (aspectAdvice != null) {
                list.add(aspectAdvice);
            }
        }
        this.aopMapping.put(method, list);
        return this;
    }

    /**
     * 根据类添加AOP映射，映射为【切入点，执行链】
     *
     * @param cls 类
     */
    public void add(Class<?> cls) {
        Assert.notNull(cls);
        if (this.adviceCache.contains(cls.getName())) {
            return;
        }
        List<Class<? extends AspectAdvice>> baseAdvices = null;
        // 如果类上有aop注解
        Aop classAop = cls.getAnnotation(Aop.class);
        if (classAop != null) {
            baseAdvices = Arrays.asList(classAop.value());
        }
        // 方法上的切面
        List<Class<? extends AspectAdvice>> advices = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            advices.clear();
            if (baseAdvices != null) {
                advices.addAll(baseAdvices);
            }

            Aop methodAop = method.getAnnotation(Aop.class);
            // 如果方法上有aop注解
            if (methodAop != null) {
                advices.addAll(Arrays.asList(methodAop.value()));
            }
            if (classAop != null || methodAop != null) {
                this.addMapping(method, advices.toArray(new Class[0]));
            }
        }
        this.adviceCache.add(cls.getName());
    }

    /**
     * 是否需要代理
     *
     * @param cls cls
     * @return boolean
     */
    public static boolean isNeedProxy(Class<?> cls) {
        if (cls.isAnnotationPresent(Aop.class)) {
            return true;
        }
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Aop.class)) {
                return true;
            }
        }
        return false;
    }


}
