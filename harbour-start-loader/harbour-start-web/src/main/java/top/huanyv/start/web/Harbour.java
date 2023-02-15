package top.huanyv.start.web;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.enums.RequestMethod;

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

    public Harbour get(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.GET, result);
        return this;
    }

    public Harbour post(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.POST, result);
        return this;
    }

    public Harbour put(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.PUT, result);
        return this;
    }

    public Harbour delete(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.DELETE, result);
        return this;
    }

    public Harbour route(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.GET, result);
        register(urlPattern, RequestMethod.POST, result);
        register(urlPattern, RequestMethod.PUT, result);
        register(urlPattern, RequestMethod.DELETE, result);
        return this;
    }

    private void register(String urlPattern, RequestMethod method, ActionResult result) {
        this.requestRegistry.registerHandler(urlPattern, method, new FunctionRequestHandler(result));
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
