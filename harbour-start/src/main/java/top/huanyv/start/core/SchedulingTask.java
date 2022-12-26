package top.huanyv.start.core;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务，默认立即执行，10s一次
 *
 * @author huanyv
 * @date 2022/12/26 15:16
 */
public interface SchedulingTask {

    /**
     * 定时的时间单位，默认为秒
     *
     * @return {@link TimeUnit}
     */
    default TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * 任务的间隔时长
     *
     * @return long
     */
    default long getPeriod() {
        return 10L;
    }

    /**
     * 得到初始延迟
     *
     * @return long
     */
    default long getInitialDelay() {
        return 0L;
    }

    /**
     * 任务
     */
    void run();

}
