package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.annotation.argument.Cookie;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.type.TypeConverter;
import top.huanyv.webmvc.core.request.type.TypeConverterFactory;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class CookieMethodArgumentResolver implements MethodArgumentResolver{
    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        return req.getCookieValue(methodParameterDesc.getAnnotation(Cookie.class).value());
    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Class<?> type = methodParameterDesc.getType();
        return methodParameterDesc.isAnnotationPresent(Cookie.class) && String.class.equals(type);
    }
}
