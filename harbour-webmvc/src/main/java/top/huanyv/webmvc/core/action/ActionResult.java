package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

/**
 * @author huanyv
 * @date 2023/2/13 16:27
 */
@FunctionalInterface
public interface ActionResult {
    void execute(HttpRequest req, HttpResponse resp) throws Exception;
}
