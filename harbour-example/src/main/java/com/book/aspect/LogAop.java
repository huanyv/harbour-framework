package com.book.aspect;

import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.bean.aop.JoinPoint;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.utils.ServletHolder;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author admin
 * @date 2022/8/5 17:19
 */
public class LogAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        Object[] args = point.getArgs();

        HttpRequest request = ServletHolder.getRequest();

        Object result = point.invoke();

        Date now = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        System.out.println(nowTime + " -------------- " + "=======Start=======");
        System.out.println(nowTime + " -------------- " + "URL            : " + request.getUri());
        System.out.println(nowTime + " -------------- " + "HTTP Method    : " + request.method());
        System.out.println(nowTime + " -------------- " + "Class Method   : " + point.getTarget().getClass().getName() + "." + point.getMethod().getName());
        System.out.println(nowTime + " -------------- " + "IP             : " + request.raw().getRemoteAddr());
        System.out.println(nowTime + " -------------- " + "Request Args   : " + Arrays.toString(args));
        System.out.println(nowTime + " -------------- " + "Response       : " + result);
        System.out.println(nowTime + " -------------- " + "========End========");

        return result;
    }
}
