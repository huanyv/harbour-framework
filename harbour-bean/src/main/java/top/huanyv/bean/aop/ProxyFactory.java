package top.huanyv.bean.aop;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @author admin
 * @date 2022/8/5 9:14
 */
public class ProxyFactory {

    public static <T> T getProxy(T target, AopContext aopContext) {
        // 如果有接口，使用JDK动态代理
        if (target.getClass().getInterfaces().length > 0) {
            return (T) Proxy.newProxyInstance(target.getClass().getClassLoader()
                    , target.getClass().getInterfaces(), new JdkInvocationHandler<T>(target, aopContext));
        }

        // 没有接口，使用CGLib动态代理
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibInvocationHandler<T>(target, aopContext));
        return (T) enhancer.create();
    }

}
