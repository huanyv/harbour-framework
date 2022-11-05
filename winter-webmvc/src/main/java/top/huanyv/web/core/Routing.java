package top.huanyv.web.core;

import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;

/**
 * @author admin
 * @date 2022/7/29 15:09
 * 路由地址注册
 */
public interface Routing {

    default Routing get(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.GET, handler);
        return this;
    }

    default Routing post(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.POST, handler);
        return this;
    }

    default Routing put(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.PUT, handler);
        return this;
    }

    default Routing delete(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.DELETE, handler);
        return this;
    }

    default Routing route(String urlPattern, ServletHandler handler) {
        register(urlPattern, RequestMethod.GET, handler);
        register(urlPattern, RequestMethod.POST, handler);
        register(urlPattern, RequestMethod.PUT, handler);
        register(urlPattern, RequestMethod.DELETE, handler);
        return this;
    }

    void register(String urlPattern, RequestMethod method, ServletHandler handler);

    default Routing view(String urlPattern, String viewName) {
        route(urlPattern, ((req, resp) -> req.view(viewName)));
        return this;
    }

}
