package top.huanyv.web.core;

import top.huanyv.tools.utils.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/11/7 21:12
 */
public class MethodRequestHandler implements RequestHandler{

    private Class<?> controller;

    private Method method;

    private Object controllerInstance;

    public MethodRequestHandler(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        if (controllerInstance == null) {
            controllerInstance = ReflectUtil.newInstance(controller);
        }
        method.invoke(controllerInstance, req, resp);
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
