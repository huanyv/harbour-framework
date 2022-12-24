package com.book;

import top.huanyv.start.web.HarbourApplicationInitializer;

/**
 * 打war包
 *
 * @author huanyv
 * @date 2022/12/24 16:51
 */
public class WebApplication extends HarbourApplicationInitializer {
    @Override
    public Class<?> run() {
        return MainApplication.class;
    }
}
