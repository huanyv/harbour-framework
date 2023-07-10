package com.book.config;

import top.huanyv.bean.annotation.Component;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;

/**
 * @author huanyv
 * @date 2022/12/26 15:28
 */
// @Component
public class Runner implements ApplicationRunner {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void run(AppArguments appArguments) {
        System.out.println("应用启动了");
    }
}
