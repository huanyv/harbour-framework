package top.huanyv.interfaces;

import top.huanyv.core.HttpRequest;
import top.huanyv.core.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface ServletHandler {
    void handle(HttpRequest req, HttpResponse resp) throws IOException;
}
