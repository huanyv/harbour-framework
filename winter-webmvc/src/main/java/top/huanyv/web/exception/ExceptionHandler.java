package top.huanyv.web.exception;

import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

/**
 * @author admin
 * @date 2022/7/28 17:40
 */
public interface ExceptionHandler {
    default void handle(HttpRequest request, HttpResponse response, Exception e) {
        e.printStackTrace();
    }
}
