package top.huanyv.logging.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

/**
 * @author huanyv
 * @date 2023/2/14 19:54
 */
public abstract class FileAppenderFactory implements AppenderFactory {

    protected static final String FILE_LOG_PATTERN = DEFAULT_LOG_PATTERN;

    @Override
    public Appender<ILoggingEvent> getAppender() {
        return getFileAppender();
    }

    public abstract FileAppender<ILoggingEvent> getFileAppender();

}
