package top.huanyv.start.web;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.interfaces.ServletHandler;

public class Harbour {

    /**
     * 单例
     */
    private Harbour() { }
    private static class SingleHolder {
        private static final Harbour INSTANCE = new Harbour();
    }
    public static Harbour use() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 请求注册器
     */
    private final RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

    public Harbour get(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.GET, handler);
        return this;
    }

    public Harbour post(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.POST, handler);
        return this;
    }

    public Harbour put(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.PUT, handler);
        return this;
    }

    public Harbour delete(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.DELETE, handler);
        return this;
    }

    public Harbour route(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.GET, handler);
        register(urlPattern, RequestMethod.POST, handler);
        register(urlPattern, RequestMethod.PUT, handler);
        register(urlPattern, RequestMethod.DELETE, handler);
        return this;
    }

    private void register(String urlPattern, RequestMethod method, ServletHandler handler) {
        this.requestRegistry.registerHandler(urlPattern, method, new FunctionRequestHandler(handler));
    }

    public Harbour view(String urlPattern, String viewName) {
        route(urlPattern, ((req, resp) -> req.view(viewName)));
        return this;
    }

    /**
     * 服务启动
     */
    public ApplicationContext run(Class<?> mainClass, String... args) {
        return HarbourApplication.run(mainClass, args);
    }

}
