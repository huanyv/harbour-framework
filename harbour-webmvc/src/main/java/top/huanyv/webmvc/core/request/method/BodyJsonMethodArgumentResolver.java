package top.huanyv.webmvc.core.request.method;

import top.huanyv.tools.bean.TypeReferenceImpl;
import top.huanyv.tools.utils.JsonUtil;
import top.huanyv.tools.utils.WebUtil;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.type.TypeConverter;
import top.huanyv.webmvc.core.request.type.TypeConverterFactory;
import top.huanyv.webmvc.enums.BodyType;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class BodyJsonMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        String requestBody = null;
        try {
            requestBody = req.body();
        } catch (IOException e) {
            return null;
        }
        Object result = null;
        ParameterizedType genericType = methodParameterDesc.getGenericType();
        // 如果是泛型类型
        if (genericType != null) {
            result = JsonUtil.fromJson(requestBody, genericType);
        } else {
            result = JsonUtil.fromJson(requestBody, methodParameterDesc.getType());
        }
        return result;

    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Body body = methodParameterDesc.getAnnotation(Body.class);
        return body != null && BodyType.JSON.equals(body.value());
    }
}
