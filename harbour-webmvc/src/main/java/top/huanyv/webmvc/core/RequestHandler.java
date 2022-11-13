package top.huanyv.webmvc.core;

/**
 * @author admin
 * @date 2022/7/24 16:52
 */
public interface RequestHandler {

    void handle(HttpRequest req, HttpResponse resp) throws Exception;

}
