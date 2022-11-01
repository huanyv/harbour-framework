package top.huanyv.ioc.utils;

import junit.framework.TestCase;
import top.huanyv.ioc.aop.AopContext;
import top.huanyv.ioc.aop.ProxyFactory;

public class AopUtilTest extends TestCase {

    public void testGetTargetObject() {
        Object o = new Object();
        AopContext aopContext = new AopContext();

        Object proxy = ProxyFactory.getProxy(ProxyFactory.getProxy(o, aopContext), aopContext);

        Object targetObject = AopUtil.getTargetObject(proxy);
        System.out.println("targetObject = " + targetObject.getClass());
    }
}