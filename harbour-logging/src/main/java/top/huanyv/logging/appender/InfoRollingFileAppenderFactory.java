package top.huanyv.logging.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.FileSize;

/**
 * @author huanyv
 * @date 2023/2/14 19:56
 */
public class InfoRollingFileAppenderFactory extends FileAppenderFactory {

    private String name;

    private String path;

    private Context context;

    public InfoRollingFileAppenderFactory(String name, String path, Context context) {
        this.name = name;
        this.path = path;
        this.context = context;
    }

    @Override
    public FileAppender<ILoggingEvent> getFileAppender() {
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setName(name);
        rollingFileAppender.setContext(context);
        rollingFileAppender.setAppend(true);

        rollingFileAppender.setFile(path + "/info.log");

        ThresholdFilter filter = new ThresholdFilter();
        filter.setContext(context);
        filter.setLevel(Level.INFO.levelStr);
        filter.start();
        rollingFileAppender.addFilter(filter);

        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setContext(context);
        levelFilter.setLevel(Level.ERROR);
        levelFilter.setOnMatch(FilterReply.DENY);
        levelFilter.setOnMismatch(FilterReply.ACCEPT);
        levelFilter.start();
        rollingFileAppender.addFilter(levelFilter);

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setFileNamePattern(path + "/%d{yyyy-MM, aux}/info.%d{yyyy-MM-dd}.log.gz");
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("5GB"));
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(FILE_LOG_PATTERN);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);

        rollingFileAppender.start();
        return rollingFileAppender;
    }
}
