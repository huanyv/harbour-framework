package top.huanyv.jdbc.core.proxy;

import top.huanyv.jdbc.builder.BaseDao;

import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/7/23 15:05
 */
public class InterfaceDaoProxyHandler extends AbstractDaoProxyHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果调用的是BaseDao的方法
        if (BaseDao.class.equals(method.getDeclaringClass())) {
            return doBaseDao(proxy, method, args);
        }
        // 如果调用Object的方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        // 注解
        return doAnnotation(method, args);
    }

}
