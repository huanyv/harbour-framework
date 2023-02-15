package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/2/13 16:28
 */
public class View implements ActionResult {

    private String viewName;

    public View(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws IOException, ServletException {
        req.view(viewName);
    }

}
