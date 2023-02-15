package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/2/13 16:28
 */
public class Text implements ActionResult {

    private Object text;

    public Text(Object text) {
        this.text = text;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws IOException, ServletException {
        resp.text(text.toString());
    }

}
