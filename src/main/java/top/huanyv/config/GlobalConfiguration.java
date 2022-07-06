package top.huanyv.config;

import cn.hutool.core.lang.ClassScanner;
import top.huanyv.annotation.Configuration;
import top.huanyv.utils.ResourceUtil;
import top.huanyv.utils.SystemConstants;

import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Set;

/**
 * @author admin
 * @date 2022/7/6 9:05
 */
public class GlobalConfiguration {

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


    private static class GlobalProperties {
        public static final Properties PROPERTIES = ResourceUtil.getProperties(SystemConstants.SYSTEM_CONFIG_FILE);
    }

    public static String getStringConfig(String key, String defaultValue) {
        return GlobalProperties.PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getIntConfig(String key, String defaultValue) {
        String value = GlobalProperties.PROPERTIES.getProperty(key, defaultValue);
        return Integer.parseInt(value);
    }


    public static WebConfiguration getWebConfigClass() {
        WebConfiguration webConfiguration = null;
        Set<Class<?>> classes = ClassScanner.scanPackageBySuper("top.huanyv", WebConfiguration.class);
        System.out.println(classes.size());
        if (classes.size() == 1) {
            webConfiguration = new WebConfiguration() {};
        } else {
            for (Class<?> aClass : classes) {
                try {
                    Configuration annotation = aClass.getAnnotation(Configuration.class);
                    if (annotation != null) {
                        webConfiguration = (WebConfiguration) aClass.newInstance();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return webConfiguration;
    }


}
