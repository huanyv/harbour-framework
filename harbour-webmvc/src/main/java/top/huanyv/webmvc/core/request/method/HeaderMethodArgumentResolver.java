package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.annotation.argument.Header;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class HeaderMethodArgumentResolver implements MethodArgumentResolver{
    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        return req.getHeader(methodParameterDesc.getAnnotation(Header.class).value());
    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Class<?> type = methodParameterDesc.getType();
        return methodParameterDesc.isAnnotationPresent(Header.class) && String.class.equals(type);
    }
}
