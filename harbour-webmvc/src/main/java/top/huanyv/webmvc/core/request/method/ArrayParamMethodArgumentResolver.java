package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.type.TypeConverter;
import top.huanyv.webmvc.core.request.type.TypeConverterFactory;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class ArrayParamMethodArgumentResolver implements MethodArgumentResolver{
    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        Class<?> type = methodParameterDesc.getType();
        String key = methodParameterDesc.getAnnotation(Param.class).value();
        String[] values = req.raw().getParameterValues(key);
        if (values == null) {
            return null;
        }
        if (String[].class.equals(type)) {
            return values;
        }
        TypeConverter typeConverter = TypeConverterFactory.getTypeConverter(String[].class, type);
        return typeConverter == null ? null : typeConverter.convert(values, methodParameterDesc);
    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Class<?> type = methodParameterDesc.getType();
        return methodParameterDesc.isAnnotationPresent(Param.class)
                && (type.isArray() || List.class.isAssignableFrom(type));
    }
}
