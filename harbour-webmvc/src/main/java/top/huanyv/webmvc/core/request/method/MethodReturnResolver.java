package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 方法返回解析器
 *
 * @author huanyv
 * @date 2022/11/15 16:12
 */
public interface MethodReturnResolver {

    void resolve(HttpRequest req, HttpResponse resp, Object returnValue, Method method) throws ServletException, IOException;

    boolean support(Method method);

}
