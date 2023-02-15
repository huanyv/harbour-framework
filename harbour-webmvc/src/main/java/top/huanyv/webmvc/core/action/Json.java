package top.huanyv.webmvc.core.action;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/2/13 16:28
 */
public class Json implements ActionResult {

    private Object data;

    public Json(Object data) {
        this.data = data;
    }

    @Override
    public void execute(HttpRequest req, HttpResponse resp) throws IOException, ServletException {
        resp.json(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
