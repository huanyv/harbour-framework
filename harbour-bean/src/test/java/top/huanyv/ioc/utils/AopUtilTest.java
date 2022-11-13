package top.huanyv.ioc.utils;

import junit.framework.TestCase;
import top.huanyv.bean.aop.AopContext;
import top.huanyv.bean.aop.ProxyFactory;
import top.huanyv.bean.utils.AopUtil;

public class AopUtilTest extends TestCase {

    public void testGetTargetObject() {
        Object o = new Object();
        AopContext aopContext = new AopContext();

        Object proxy = ProxyFactory.getProxy(ProxyFactory.getProxy(o, aopContext), aopContext);

        Object targetObject = AopUtil.getTargetObject(proxy);
        System.out.println("targetObject = " + targetObject.getClass());
    }
}