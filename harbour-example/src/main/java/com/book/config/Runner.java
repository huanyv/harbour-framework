package com.book.config;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Order;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;

/**
 * @author huanyv
 * @date 2022/12/26 15:28
 */
@Component
@Order(0)
public class Runner implements ApplicationRunner {
    @Override
    public void run(AppArguments appArguments) {
        System.out.println("应用启动了");
    }
}
