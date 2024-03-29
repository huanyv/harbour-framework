package top.huanyv.webmvc.guard;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.RequestHandler;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public interface NavigationGuard {

    default boolean beforeEach(HttpRequest req, HttpResponse resp, RequestHandler handler) throws Exception {
        return true;
    }

    default void afterEach(HttpRequest req, HttpResponse resp, RequestHandler handler) throws Exception {

    }

}
