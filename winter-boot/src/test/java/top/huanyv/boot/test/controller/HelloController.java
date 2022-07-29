package top.huanyv.boot.test.controller;

import top.huanyv.ioc.anno.Component;
import top.huanyv.web.anno.Route;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import java.io.IOException;

/**
 * @author admin
 * @date 2022/7/29 16:52
 */
@Component
@Route
public class HelloController {

    @Route("/hello")
    public void hello(HttpRequest req, HttpResponse resp) throws IOException {
        resp.html("<h1>hello world<h1/>");
    }

}
