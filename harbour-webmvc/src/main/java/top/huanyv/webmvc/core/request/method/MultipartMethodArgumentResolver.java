package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class MultipartMethodArgumentResolver implements MethodArgumentResolver{
    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        Class<?> type = methodParameterDesc.getType();
        if (Part.class.equals(type)) {
            String key = methodParameterDesc.getAnnotation(Param.class).value();
            return req.getPart(key);
        }
        return req.getParts();

    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Class<?> type = methodParameterDesc.getType();
        Type genericType = methodParameterDesc.getGenericType();
        return (List.class.equals(type)
                && genericType != null
                && Part.class.equals(methodParameterDesc.getActualTypes()[0]))
                || (Part.class.equals(type) && methodParameterDesc.isAnnotationPresent(Param.class));
    }
}
