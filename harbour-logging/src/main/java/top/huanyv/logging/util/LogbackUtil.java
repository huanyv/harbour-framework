package top.huanyv.logging.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.encoder.Encoder;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import top.huanyv.logging.appender.AppenderFactory;

/**
 * @author huanyv
 * @date 2023/2/15 19:24
 */
public final class LogbackUtil {
    private LogbackUtil() {
    }

    public static LoggerContext getContext() {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext) {
            return (LoggerContext) iLoggerFactory;
        }
        return null;
    }

    public static void setAppender(Logger logger, AppenderFactory factory) {
        logger.addAppender(factory.getAppender());
    }

    public static void setAppenderEncoderPattern(Encoder<?> encoder, String pattern) {
        if (encoder instanceof PatternLayoutEncoder) {
            PatternLayoutEncoder patternLayoutEncoder = (PatternLayoutEncoder) encoder;
            patternLayoutEncoder.setPattern(pattern);
            patternLayoutEncoder.start();
        }
    }

    public static void replaceJul() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

}
