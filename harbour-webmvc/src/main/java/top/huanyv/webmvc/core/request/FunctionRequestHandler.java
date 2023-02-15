package top.huanyv.webmvc.core.request;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.action.ActionResult;

/**
 * @author huanyv
 * @date 2022/11/7 21:16
 */
public class FunctionRequestHandler implements RequestHandler {

    private ActionResult actionResult;

    public FunctionRequestHandler(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        actionResult.execute(req, resp);
    }
}
