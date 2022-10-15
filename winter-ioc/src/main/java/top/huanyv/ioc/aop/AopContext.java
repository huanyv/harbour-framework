package top.huanyv.ioc.aop;

import top.huanyv.utils.ReflectUtil;

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
    private Map<Method, List<AspectAdvice>> aopMapping = new ConcurrentHashMap<>();

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

    public boolean hasProxy(Method method) {
        return aopMapping.containsKey(method);
    }

    /**
     * 判断类是否需要代理
     *
     * @param cls cls
     * @return boolean
     */
    public boolean hasProxy(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            return aopMapping.containsKey(method);
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

    public void add(Class<?> cls) {
        List<Class<? extends AspectAdvice>> advices = new ArrayList<>();
        // 如果类上有aop注解
        Aop classAop = cls.getAnnotation(Aop.class);
        if (classAop != null) {
            advices.addAll(Arrays.asList(classAop.value()));
        }
        // 方法上的切面
        for (Method method : cls.getDeclaredMethods()) {
            Aop methodAop = method.getAnnotation(Aop.class);
            // 如果方法上有aop注解
            if (methodAop != null) {
                advices.addAll(Arrays.asList(methodAop.value()));
            }
            this.addMapping(method, advices.toArray(new Class[0]));
        }
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