package org.example.service.impl;

import org.example.service.SayService;
import top.huanyv.rpc.annotation.Provider;

/**
 * @author huanyv
 * @date 2023/1/22 16:16
 */
@Provider(interfaceClass = SayService.class)
public class HelloService implements SayService {
    @Override
    public String sayHello(String name) {
        // try {
        //     Thread.sleep(4000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        return "20880 " + name + "!";
    }
}
