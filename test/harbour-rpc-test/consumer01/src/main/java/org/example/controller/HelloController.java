package org.example.controller;

import org.example.service.SayService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.rpc.annotation.Reference;
import top.huanyv.rpc.load.Balance;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Param;

/**
 * @author huanyv
 * @date 2023/1/22 16:27
 */
@Component
@Route
public class HelloController {

    @Inject
    @Reference(loadBalance = Balance.ROUND, timeout = 1)
    private SayService service;

    @Get("/hello")
    @Body
    public String hello(@Param("name") String name) {
        String s = service.sayHello(name);
        System.out.println("s = " + s);
        return s;
    }
}
