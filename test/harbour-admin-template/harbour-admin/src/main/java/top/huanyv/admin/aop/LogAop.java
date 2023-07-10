package top.huanyv.admin.aop;

import lombok.extern.slf4j.Slf4j;
import top.huanyv.bean.aop.AspectAdvice;
import top.huanyv.bean.aop.JoinPoint;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.utils.ServletHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Slf4j
public class LogAop implements AspectAdvice {
    @Override
    public Object aroundAdvice(JoinPoint point) throws InvocationTargetException, IllegalAccessException {
        Object[] args = point.getArgs();

        HttpRequest request = ServletHolder.getRequest();

        Object result = point.invoke();

        log.info("=======Start=======");
        log.info("URL            : " + request.getUri());
        log.info("HTTP Method    : " + request.method());
        log.info("Class Method   : " + point.getTarget().getClass().getName() + "." + point.getMethod().getName());
        log.info("IP             : " + request.raw().getRemoteAddr());
        log.info("Request Args   : " + Arrays.toString(args));
        log.info("Response       : " + result);
        log.info("========End========");

        return result;
    }
}
