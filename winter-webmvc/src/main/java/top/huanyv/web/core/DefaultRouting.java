package top.huanyv.web.core;

import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;

/**
 * @author admin
 * @date 2022/7/29 15:13
 */
public class DefaultRouting implements Routing {

    /**
     * 请求注册器
     */
    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

    @Override
    public void register(String urlPattern, RequestMethod method, ServletHandler handler) {
        requestRegistry.registerHandler(urlPattern, method, new FunctionRequestHandler(handler));
    }
}
