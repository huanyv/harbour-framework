package top.huanyv.logging.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * @author huanyv
 * @date 2023/2/14 19:53
 */
public interface AppenderFactory {

    String DEFAULT_LOG_PATTERN = "%date{yyyy-MM-dd HH:mm:ss.sss} %-5level [%15.15thread] %-35.35class{36} : %msg%n";

    Appender<ILoggingEvent> getAppender();

}

