package top.huanyv.web.interfaces;

import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface ServletHandler {
    void handle(HttpRequest req, HttpResponse resp) throws IOException;
}
