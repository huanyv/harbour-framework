package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

/**
 * @author huanyv
 * @date 2023/2/13 16:34
 */
public class Redirect implements ActionResult {
    private String location;

    public Redirect(String location) {
        this.location = location;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws Exception {
        resp.redirect(location);
    }
}
