package top.huanyv.webmvc.core.request.method;

import top.huanyv.tools.utils.JsonUtil;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.enums.BodyType;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/15 16:29
 */
public class BodyJsonMethodReturnResolver implements MethodReturnResolver {

    @Override
    public void resolve(HttpRequest req, HttpResponse resp, Object returnValue, Method method) throws ServletException, IOException {
        if (returnValue == null) {
            return;
        }
        resp.getWriter().println(JsonUtil.toJson(returnValue));
    }

    @Override
    public boolean support(Method method) {
        Body body = method.getAnnotation(Body.class);
        if (body == null) {
            body = method.getDeclaringClass().getAnnotation(Body.class);
        }
        return body != null && BodyType.JSON.equals(body.value());
    }

}
