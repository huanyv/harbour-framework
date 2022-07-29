package top.huanyv.web.core;

import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;

/**
 * @author admin
 * @date 2022/7/29 15:13
 */
public class DefaultWinter implements Winter{

    /**
     * 请求注册器
     */
    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

    @Override
    public Winter get(String urlPattern, ServletHandler handler) {
        register(urlPattern, handler, RequestMethod.GET);
        return this;
    }

    @Override
    public Winter post(String urlPattern, ServletHandler handler) {
        register(urlPattern, handler, RequestMethod.POST);
        return this;
    }

    @Override
    public Winter put(String urlPattern, ServletHandler handler) {
        register(urlPattern, handler, RequestMethod.PUT);
        return this;
    }

    @Override
    public Winter delete(String urlPattern, ServletHandler handler) {
        register(urlPattern, handler, RequestMethod.DELETE);
        return this;
    }

    @Override
    public Winter route(String urlPattern, ServletHandler handler) {
        register(urlPattern, handler, RequestMethod.GET);
        register(urlPattern, handler, RequestMethod.POST);
        register(urlPattern, handler, RequestMethod.PUT);
        register(urlPattern, handler, RequestMethod.DELETE);
        return this;
    }

    @Override
    public void register(String urlPattern, ServletHandler handler, RequestMethod method) {
        requestRegistry.register(urlPattern, method, handler);
    }
}
