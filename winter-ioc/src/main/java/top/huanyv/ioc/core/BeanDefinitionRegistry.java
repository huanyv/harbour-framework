package top.huanyv.ioc.core;

import jdk.nashorn.internal.ir.IfNode;
import top.huanyv.ioc.aop.Aop;
import top.huanyv.ioc.aop.AspectAdvice;
import top.huanyv.ioc.exception.BeanTypeNonUniqueException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2022/8/8 14:50
 */
public class BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private Map<Method, AspectAdvice> aopMethodMapping = new HashMap<>();

    public void register(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        try {
            Class<?> clazz = beanDefinition.getBeanClass();
            Aop classAop = clazz.getAnnotation(Aop.class);
            AspectAdvice classAdvice = null;
            if (classAop != null) {
                classAdvice = classAop.value().getConstructor().newInstance();
            }
            for (Method method : clazz.getDeclaredMethods()) {
                Aop aop = method.getAnnotation(Aop.class);
                if (aop != null) {
                    Class<? extends AspectAdvice> value = aop.value();
                    AspectAdvice advice = value.getConstructor().newInstance();
                    aopMethodMapping.put(method, advice);
                } else {
                    if (classAop != null) {
                        aopMethodMapping.put(method, classAdvice);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public AspectAdvice getBeanAspect(Method method) {
        return this.aopMethodMapping.get(method);
    }


    public boolean isNeedProxy(Class<?> beanClass, Method method) {
        if (beanClass.isAnnotationPresent(Aop.class)) {
            return true;
        }
        try {
            if (beanClass.getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Aop.class)) {
                return true;
            }
        } catch (NoSuchMethodException e) {
            return false;
        }
        return false;
    }

    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    public String getBeanNameByType(Class<?> type) {
        String result = null;
        int count = 0;
        for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
            Class<?> beanClass = entry.getValue().getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                count++;
                if(count >= 2) {
                    throw new BeanTypeNonUniqueException(beanClass);
                }
                result = entry.getKey();
            }
        }
        return result;
    }

}
