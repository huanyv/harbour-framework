package com.book.config;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;

/**
 * @author huanyv
 * @date 2022/12/26 15:28
 */
// @Bean
public class Runner implements ApplicationRunner {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void run(Configuration appArguments) {
        System.out.println("应用启动了");
    }
}
