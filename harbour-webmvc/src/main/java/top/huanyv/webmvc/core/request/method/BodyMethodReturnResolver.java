package top.huanyv.webmvc.core.request.method;

import top.huanyv.tools.utils.JsonUtil;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/15 16:29
 */
public class BodyMethodReturnResolver implements MethodReturnResolver {

    @Override
    public void resolve(HttpRequest req, HttpResponse resp, Object returnValue, Method method) throws ServletException, IOException {
        if (returnValue == null) {
            return;
        }
        resp.getWriter().println(JsonUtil.toJson(returnValue));
    }

    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(Body.class) || method.getDeclaringClass().isAnnotationPresent(Body.class);
    }

}
