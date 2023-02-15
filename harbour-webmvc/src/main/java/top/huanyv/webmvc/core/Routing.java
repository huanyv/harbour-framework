package top.huanyv.webmvc.core;

import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.enums.RequestMethod;

/**
 * @author admin
 * @date 2022/7/29 15:09
 * 路由地址注册
 */
public interface Routing {

    default Routing get(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.GET, result);
        return this;
    }

    default Routing post(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.POST, result);
        return this;
    }

    default Routing put(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.PUT, result);
        return this;
    }

    default Routing delete(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.DELETE, result);
        return this;
    }

    default Routing route(String urlPattern, ActionResult result) {
        register(urlPattern, RequestMethod.GET, result);
        register(urlPattern, RequestMethod.POST, result);
        register(urlPattern, RequestMethod.PUT, result);
        register(urlPattern, RequestMethod.DELETE, result);
        return this;
    }

    void register(String urlPattern, RequestMethod method, ActionResult result);

    default Routing view(String urlPattern, String viewName) {
        route(urlPattern, ((req, resp) -> req.view(viewName)));
        return this;
    }

    default void setBase(String base) {

    }

}
