package com.book.aspect;

import top.huanyv.ioc.aop.AdvicePoint;
import top.huanyv.ioc.aop.BaseAop;
import top.huanyv.utils.WebUtil;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2022/8/5 17:19
 */
public class LogAop implements BaseAop {
    @Override
    public Object aroundAdvice(AdvicePoint point) throws InvocationTargetException, IllegalAccessException {
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

        System.out.println("=======Start=======");
        System.out.println("URL            : " + WebUtil.getRequestURI(request.getOriginal()));
        System.out.println("HTTP Method    : " + request.getMethod());
        System.out.println("Class Method   : " + point.getTarget().getClass().getName() + "." + point.getMethod().getName());
        System.out.println("IP             : " + request.getRemoteAddr());
        System.out.println("Request Args   : " + request.getParameterMap().entrySet()
                .stream().map(entry -> entry.getKey() + Arrays.toString(entry.getValue()))
                .collect(Collectors.toList()));
        System.out.println("=======End========");

        Object result = point.invoke();

        return result;
    }
}
