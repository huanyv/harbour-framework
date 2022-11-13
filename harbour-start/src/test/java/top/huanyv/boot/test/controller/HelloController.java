package top.huanyv.boot.test.controller;


import top.huanyv.bean.annotation.Component;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

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
