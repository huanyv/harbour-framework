package top.huanyv.webmvc.core.request;

import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.method.*;
import top.huanyv.webmvc.utils.ClassDesc;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/7 21:12
 */
public class MethodRequestHandler implements RequestHandler {

    private Class<?> controller;

    private Method method;

    private Object controllerInstance;

    private MethodArgumentResolverComposite argumentResolver;

    private MethodReturnResolverComposite returnResolver;

    public MethodRequestHandler(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws InvocationTargetException, IllegalAccessException, ServletException, IOException {
        method.setAccessible(true);
        if (controllerInstance == null) {
            controllerInstance = ReflectUtil.newInstance(controller);
        }

        initResolver();

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

        if (returnResolver.support(method)) {
            returnResolver.resolve(req, resp, returnValue, method);
        }

    }

    private void initResolver() {
        if (argumentResolver == null) {
            argumentResolver = new MethodArgumentResolverComposite().addResolvers(getMethodArgumentResolver());
        }
        if (returnResolver == null) {
            returnResolver = new MethodReturnResolverComposite();
        }
    }

    private List<MethodArgumentResolver> getMethodArgumentResolver() {
        List<MethodArgumentResolver> result = new ArrayList<>();
        result.add(new ParamMethodArgumentResolver());
        result.add(new ArrayParamMethodArgumentResolver());
        result.add(new BodyMethodArgumentResolver());
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

    public void setController(Class<?> controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public void setControllerInstance(Object controllerInstance) {
        this.controllerInstance = controllerInstance;
    }
}
