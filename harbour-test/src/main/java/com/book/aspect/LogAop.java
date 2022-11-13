package com.book.aspect;

import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.bean.aop.JoinPoint;
import top.huanyv.tools.utils.WebUtil;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2022/8/5 17:19
 */
public class LogAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        Object[] args = point.getArgs();
        HttpRequest request = null;
        HttpResponse response = null;
        for (Object arg : args) {
            if (arg instanceof HttpRequest) {
                request = (HttpRequest) arg;
            }
            if (arg instanceof HttpResponse) {
                response = (HttpResponse) arg;
            }
        }

        Object result = point.invoke();

        Date now = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        System.out.println(nowTime + " -------------- " + "=======Start=======");
        System.out.println(nowTime + " -------------- " + "URL            : " + WebUtil.getRequestURI(request.getOriginal()));
        System.out.println(nowTime + " -------------- " + "HTTP Method    : " + request.getMethod());
        System.out.println(nowTime + " -------------- " + "Class Method   : " + point.getTarget().getClass().getName() + "." + point.getMethod().getName());
        System.out.println(nowTime + " -------------- " + "IP             : " + request.getRemoteAddr());
        System.out.println(nowTime + " -------------- " + "Request Args   : " + request.getParameterMap().entrySet()
                .stream().map(entry -> entry.getKey() + Arrays.toString(entry.getValue()))
                .collect(Collectors.toList()));
        System.out.println(nowTime + " -------------- " + "========End========");

        return result;
    }
}
