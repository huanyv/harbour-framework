package top.huanyv.ioc.aop;

import net.sf.cglib.proxy.Enhancer;
import top.huanyv.ioc.core.BeanDefinition;
import top.huanyv.ioc.core.BeanDefinitionRegistry;

import java.lang.reflect.Proxy;

/**
 * @author admin
 * @date 2022/8/5 9:14
 */
public class ProxyFactory {
    public static <T> T getProxy(T target, AopContext aopContext ) {
        if (target.getClass().getInterfaces().length > 0) {
            return (T)Proxy.newProxyInstance(target.getClass().getClassLoader()
                    , target.getClass().getInterfaces(), new JdkInvocationHandler<T>(target ,aopContext));
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibInvocationHandler<T>(target, aopContext));
        return (T) enhancer.create();

    }
}
