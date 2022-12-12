package top.huanyv.webmvc.core.request.method;

import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.type.MapToBeanConverter;
import top.huanyv.webmvc.core.request.type.TypeConverter;
import top.huanyv.webmvc.core.request.type.TypeConverterFactory;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;

/**
 * @author huanyv
 * @date 2022/11/17 16:09
 */
public class FormMethodArgumentResolver implements MethodArgumentResolver{

    private static final MapToBeanConverter CONVERTER = new MapToBeanConverter();

    @Override
    public Object resolve(HttpRequest req, HttpResponse resp, ClassDesc methodParameterDesc) throws ServletException, IOException {
        return CONVERTER.convert(req.raw().getParameterMap(), methodParameterDesc);
    }

    @Override
    public boolean support(ClassDesc methodParameterDesc) {
        return methodParameterDesc.isAnnotationPresent(Form.class) && BeanUtil.isJavaBean(methodParameterDesc.getType());
    }

}
