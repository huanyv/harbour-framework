package top.huanyv.boot.test.controller;

import top.huanyv.ioc.anno.Component;
import top.huanyv.web.core.RouteRegistry;
import top.huanyv.web.core.Routing;

import javax.servlet.ServletException;

/**
 * @author admin
 * @date 2022/7/30 14:54
 */
@Component
public class TestController implements RouteRegistry {

    @Override
    public void run(Routing app) {
        app.get("/test", (req, resp) -> {
            req.forward("/admin");
        });
    }


}
