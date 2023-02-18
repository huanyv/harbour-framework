package top.huanyv.start.loader.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.logging.appender.AppenderFactory;
import top.huanyv.logging.appender.ErrorRollingFileAppenderFactory;
import top.huanyv.logging.appender.InfoRollingFileAppenderFactory;
import top.huanyv.logging.util.LogbackUtil;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.tools.utils.SystemUtil;

@Properties(prefix = "harbour.log.")
public class LoggingStartLoader implements ApplicationLoader {

    static {
        LogbackUtil.replaceJul();
    }

    private static final String LEVEL_PREFIX = "harbour.log.level.";

    private String path;

    @Override
    public void load(ApplicationContext applicationContext, AppArguments appArguments) {
        LoggerContext loggerContext = LogbackUtil.getContext();
        if (loggerContext != null) {
            // 设置日志级别
            String levelConfigName = getLevelConfigName(appArguments);
            if (StringUtil.hasText(levelConfigName)) {
                String loggerName = StringUtil.removePrefix(levelConfigName, LEVEL_PREFIX);
                String levelValue = appArguments.get(levelConfigName);
                Logger logger = loggerContext.getLogger(loggerName);
                logger.setLevel(Level.toLevel(levelValue));
            }
            // 日志输出目录
            Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
            if (StringUtil.hasText(this.path)) {
                LogbackUtil.setAppender(root, new InfoRollingFileAppenderFactory("RollingFileInfo", path, loggerContext));
                LogbackUtil.setAppender(root, new ErrorRollingFileAppenderFactory("RollingFileError", path, loggerContext));
            }

            // windows日志非彩色
            if (SystemUtil.isWindows() && !SystemUtil.isIntellijIDEA()) {
                ConsoleAppender<ILoggingEvent> consoleAppender = (ConsoleAppender<ILoggingEvent>) root.getAppender("STDOUT");
                LogbackUtil.setAppenderEncoderPattern(consoleAppender.getEncoder(), AppenderFactory.DEFAULT_LOG_PATTERN);
            }
        }

    }

    private String getLevelConfigName(AppArguments appArguments) {
        for (String name : appArguments.getNames()) {
            if (name.startsWith(LEVEL_PREFIX)) {
                return name;
            }
        }
        return null;
    }
}
