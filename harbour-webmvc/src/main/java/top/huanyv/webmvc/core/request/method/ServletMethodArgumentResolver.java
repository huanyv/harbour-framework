package top.huanyv.webmvc.core.request.method;

import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.Model;
import top.huanyv.webmvc.core.request.type.MapToBeanConverter;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class ServletMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        Class<?> paramType = methodParameterDesc.getType();
        if (HttpServletRequest.class.equals(paramType)) {
            return req.getOriginal();
        } else if (HttpServletResponse.class.equals(paramType)) {
            return resp.getOriginal();
        } else if (HttpSession.class.equals(paramType)) {
            return req.getSession();
        } else if (OutputStream.class.isAssignableFrom(paramType)) {
            return resp.getOutputStream();
        } else if (HttpRequest.class.equals(paramType)) {
            return req;
        } else if (HttpResponse.class.equals(paramType)) {
            return resp;
        } else if (Model.class.equals(paramType)) {
            return new Model(req.getOriginal());
        }
        return null;
    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        Class<?> paramType = methodParameterDesc.getType();
        return ServletResponse.class.isAssignableFrom(paramType)
                || ServletRequest.class.isAssignableFrom(paramType)
                || HttpSession.class.isAssignableFrom(paramType)
                || OutputStream.class.isAssignableFrom(paramType)
                || HttpRequest.class.equals(paramType)
                || HttpResponse.class.equals(paramType)
                || Model.class.equals(paramType);
    }

}
