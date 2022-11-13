package top.huanyv.webmvc.interfaces;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

@FunctionalInterface
public interface ServletHandler {
    void handle(HttpRequest req, HttpResponse resp) throws Exception;
}
