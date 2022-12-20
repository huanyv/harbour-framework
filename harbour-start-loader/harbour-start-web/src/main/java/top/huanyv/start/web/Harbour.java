package top.huanyv.start.web;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.webmvc.core.Routing;
import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.interfaces.ServletHandler;

public class Harbour implements Routing {

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

    @Override
    public void register(String urlPattern, RequestMethod method, ServletHandler handler) {
        this.requestRegistry.registerHandler(urlPattern, method, new FunctionRequestHandler(handler));
    }

    /**
     * 服务启动
     */
    public ApplicationContext run(Class<?> mainClass, String... args) {
        return HarbourApplication.run(mainClass, args);
    }

}
