package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

/**
 * @author huanyv
 * @date 2023/2/13 16:34
 */
public class Forward implements ActionResult {
    private String path;

    public Forward(String path) {
        this.path = path;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        req.forward(path);
    }
}
