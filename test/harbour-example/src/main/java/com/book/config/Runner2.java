package com.book.config;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;

/**
 * @author huanyv
 * @date 2022/12/26 15:28
 */
// @Bean
public class Runner2 implements ApplicationRunner {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void run(AppArguments appArguments) {
        System.out.println("应用启动了2");
    }
}
