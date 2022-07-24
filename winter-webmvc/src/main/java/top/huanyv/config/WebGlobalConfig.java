package top.huanyv.config;

import top.huanyv.utils.PropertiesUtil;
import top.huanyv.utils.SystemConstants;

import java.util.Properties;

/**
 * @author admin
 * @date 2022/7/6 9:05
 */
public class WebGlobalConfig {

    public static final String DEFAULT_GLOBAL_ENCODING = "UTF-8";
    public static final String DEFAULT_SERVER_PORT = "8080";
    public static final String DEFAULT_SERVLET_CONTEXT = "";

    public static final String DEFAULT_THYMELEAF_PREFIX = "templates";
    public static final String DEFAULT_THYMELEAF_SUFFIX = ".html";
    public static final String DEFAULT_STATIC_PREFIX = "static";



    public static final String CONFIG_KEY_SERVER_PORT = "server.port";
    public static final String CONFIG_KEY_SERVER_CONTEXT = "server.context";
    public static final String CONFIG_KEY_GLOBAL_ENCODING = "server.encoding";
    public static final String CONFIG_KEY_THYMELEAF_PREFIX = "web.thymeleaf.prefix";
    public static final String CONFIG_KEY_THYMELEAF_SUFFIX = "web.thymeleaf.suffix";
    public static final String CONFIG_KEY_STATIC_PREFIX = "web.static.prefix";

    public static final String WEB_APPLICATION_CONTEXT_ATTR_NAME = "webApplicationContext";

    private static class GlobalProperties {
        public static final Properties PROPERTIES = PropertiesUtil.getProperties(SystemConstants.SYSTEM_CONFIG_FILE);
    }

    public static String getConfig(String key, String defaultValue) {
        return GlobalProperties.PROPERTIES.getProperty(key, defaultValue);
    }

}
