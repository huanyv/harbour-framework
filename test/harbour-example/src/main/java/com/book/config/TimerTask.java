package com.book.config;

import top.huanyv.start.core.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author huanyv
 * @date 2022/12/26 15:26
 */
//@Component
public class TimerTask implements Timer {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public long getPeriod() {
        return 3L;
    }

    @Override
    public void run() {
        System.out.println("定时任务：" + dateFormat.format(new Date()));
    }
}
