package com.book;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import top.huanyv.start.core.HarbourApplication;

public class MainApplication {

    private static final Log log = LogFactory.getLog(MainApplication.class);

    public static void main(String[] args) {
        HarbourApplication.run(MainApplication.class, args);
        log.info("启动成功!");
    }

}
