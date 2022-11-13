package top.huanyv.webmvc.core;

import top.huanyv.webmvc.interfaces.ServletHandler;

/**
 * @author huanyv
 * @date 2022/11/7 21:16
 */
public class FunctionRequestHandler implements RequestHandler {

    private ServletHandler servletHandler;

    public FunctionRequestHandler(ServletHandler servletHandler) {
        this.servletHandler = servletHandler;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        servletHandler.handle(req, resp);
    }
}
