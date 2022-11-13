package top.huanyv.boot.test.controller;


import top.huanyv.bean.annotation.Component;
import top.huanyv.webmvc.core.RouteRegistry;
import top.huanyv.webmvc.core.Routing;

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
