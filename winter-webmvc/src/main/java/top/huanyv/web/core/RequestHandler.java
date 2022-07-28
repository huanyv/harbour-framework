package top.huanyv.web.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author admin
 * @date 2022/7/24 16:52
 */
public class RequestHandler {

    // 请求处理适配器，就是请求所在的对象
    private Object adapter;

    // 请求对应的方法
    private Method handler;

    public RequestHandler() {
    }

    public RequestHandler(Object adapter, Method handler) {
        this.adapter = adapter;
        this.handler = handler;
    }

    public Object getAdapter() {
        return adapter;
    }

    public void setAdapter(Object adapter) {
        this.adapter = adapter;
    }

    public Method getHandler() {
        return handler;
    }

    public void setHandler(Method handler) {
        this.handler = handler;
    }

    public void handle(Object... args) throws InvocationTargetException, IllegalAccessException {
        this.handler.setAccessible(true);
        this.handler.invoke(this.adapter, args);
    }

}
