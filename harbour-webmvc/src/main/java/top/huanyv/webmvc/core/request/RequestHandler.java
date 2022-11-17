package top.huanyv.webmvc.core.request;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

/**
 * @author admin
 * @date 2022/7/24 16:52
 */
public interface RequestHandler {

    void handle(HttpRequest req, HttpResponse resp) throws Exception;

}
