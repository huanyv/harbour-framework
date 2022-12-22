package top.huanyv.webmvc.core;

import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.interfaces.ServletHandler;

/**
 * @author admin
 * @date 2022/7/29 15:13
 */
public class DefaultRouting implements Routing {

    private String base;

    public DefaultRouting() {
        this.base = "";
    }

    /**
     * 请求注册器
     */
    private final RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

    @Override
    public void register(String urlPattern, RequestMethod method, ServletHandler handler) {
        requestRegistry.registerHandler(base + urlPattern, method, new FunctionRequestHandler(handler));
    }

    @Override
    public void setBase(String base) {
        this.base = base;
    }

}
