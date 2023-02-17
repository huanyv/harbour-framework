package top.huanyv.webmvc.exception;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import java.io.IOException;

/**
 * @author admin
 * @date 2022/7/28 17:40
 */
public interface ExceptionHandler {
    default void handle(HttpRequest request, HttpResponse response, Exception e) {
        e.printStackTrace();
        try {
            response.error(500, e.getMessage());
        } catch (IOException ignored) {
        }
    }
}
