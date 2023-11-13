package top.huanyv.bean.test.factory;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Value;
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

    @Value("server.port")
    private int port;

    @Override
    public Object getObject() throws Exception {
        System.out.println(this.getClass() + "-port = " + port);
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
