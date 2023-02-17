package top.huanyv.webmvc.core.request;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.method.*;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/7 21:12
 */
public class MethodRequestHandler implements RequestHandler {

    // IOC容器
    private ApplicationContext context;

    // 控制器类型
    private Class<?> controller;

    // 控制器方法
    private Method method;

    // 请求参数解析器
    private MethodArgumentResolverComposite argumentResolver;

    // 控制器方法返回值解析器
    private MethodReturnResolverComposite returnResolver;

    public MethodRequestHandler(ApplicationContext context, Class<?> controller, Method method) {
        this.context = context;
        this.controller = controller;
        this.method = method;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        method.setAccessible(true);

        // 获取控制器实例
        Object controllerInstance = context.getBean(controller);

        // 初始化解析器
        initResolver();

        // 请求参数解析
        List<ClassDesc> methodParameters = ClassDesc.parseMethodParameter(method);
        Object[] args = new Object[methodParameters.size()];
        int index = 0;
        for (ClassDesc methodParameter : methodParameters) {
            if (argumentResolver.support(methodParameter)) {
                args[index] = argumentResolver.resolve(req, resp, methodParameter);
            } else {
                args[index] = null;
            }
            index++;
        }

        Object returnValue = method.invoke(controllerInstance, args);

        // 控制器方法返回值解析
        if (returnValue instanceof ActionResult) {
            ((ActionResult) returnValue).execute(req, resp);
            return;
        }
        if (returnResolver.support(method)) {
            returnResolver.resolve(req, resp, returnValue, method);
        }
    }

    private void initResolver() {
        if (argumentResolver == null) {
            argumentResolver = new MethodArgumentResolverComposite().addResolvers(getMethodArgumentResolvers());
        }
        if (returnResolver == null) {
            returnResolver = new MethodReturnResolverComposite();
        }
    }

    private List<MethodArgumentResolver> getMethodArgumentResolvers() {
        List<MethodArgumentResolver> result = new ArrayList<>();
        result.add(new ParamMethodArgumentResolver());
        result.add(new ArrayParamMethodArgumentResolver());
        result.add(new BodyJsonMethodArgumentResolver());
        result.add(new FormMethodArgumentResolver());
        result.add(new MultipartMethodArgumentResolver());
        result.add(new PathMethodArgumentResolver());
        result.add(new CookieMethodArgumentResolver());
        result.add(new HeaderMethodArgumentResolver());
        result.add(new ServletMethodArgumentResolver());
        return result;
    }

    public Class<?> getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return controller.getName() + "#" + method.getName() + "()";
    }
}
