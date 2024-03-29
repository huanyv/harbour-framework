package top.huanyv.jdbc.core.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import top.huanyv.jdbc.builder.BaseDao;

import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/7/23 15:05
 */
public class ClassDaoProxyHandler extends AbstractDaoProxyHandler implements MethodInterceptor {

    private Object target;

    public ClassDaoProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 如果调用的是BaseDao的方法
        if (BaseDao.class.equals(method.getDeclaringClass())) {
            return doBaseDao(proxy, method, args);
        }
        // 如果方法上使用了CRUD注解
        method = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        Object result = doAnnotation(method, args);
        // 没有用注解，执行方法体
        return result != null ? result : method.invoke(target, args);
    }
}
