package top.huanyv.web.guard;

import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public interface NavigationGuard {

    default boolean beforeEach(HttpRequest req, HttpResponse resp) {
        return true;
    }

    void afterEach(HttpRequest req, HttpResponse resp);

}
