package top.huanyv.webmvc.core.request.method;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2022/11/14 19:54
 */
public interface MethodArgumentResolver {

    Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException;

    boolean support(ClassDesc methodParameterDesc);

}
