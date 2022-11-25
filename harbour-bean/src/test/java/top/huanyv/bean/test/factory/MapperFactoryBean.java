package top.huanyv.bean.test.factory;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.ioc.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author huanyv
 * @date 2022/11/18 14:49
 */
public class MapperFactoryBean implements FactoryBean {

    private Class<?> mapperInterface;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }
                return null;
            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    public MapperFactoryBean(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

}
