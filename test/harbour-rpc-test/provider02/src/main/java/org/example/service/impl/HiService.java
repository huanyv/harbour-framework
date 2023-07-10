package org.example.service.impl;

import org.example.service.SayService;
import top.huanyv.rpc.annotation.Provider;

/**
 * @author huanyv
 * @date 2023/1/22 16:22
 */
@Provider(interfaceClass = SayService.class)
public class HiService implements SayService {
    @Override
    public String sayHello(String name) {
        return "20881 " + name + "!";
    }
}
