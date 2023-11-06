package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.utils.JsonUtil;
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
public class ViewMethodReturnResolver implements MethodReturnResolver {

    public static final String FORWARD_VIEW_PREFIX = "forward:";

    public static final String REDIRECT_VIEW_PREFIX = "redirect:";

    @Override
    public void resolve(HttpRequest req, HttpResponse resp, Object returnValue, Method method) throws ServletException, IOException {
        if (returnValue == null) {
            return;
        }
        String view = (String) returnValue;
        if (view.startsWith(FORWARD_VIEW_PREFIX)) {
            req.forward(view.substring(FORWARD_VIEW_PREFIX.length()));
        } else if (view.startsWith(REDIRECT_VIEW_PREFIX)) {
            resp.redirect(view.substring(REDIRECT_VIEW_PREFIX.length()));
        } else {
            req.view(view);
        }
    }

    @Override
    public boolean support(Method method) {
        return !method.isAnnotationPresent(Body.class)
                && !method.getDeclaringClass().isAnnotationPresent(Body.class)
                && String.class.equals(method.getReturnType());
    }

}
