package top.huanyv.config;

/**
 * @author admin
 * @date 2022/7/6 8:56
 */
public interface WebConfiguration {

    default int getServerPort() {
        return Integer.parseInt(GlobalConfiguration.DEFAULT_SERVER_PORT);
    }

    default String getServerContext() {
        return GlobalConfiguration.DEFAULT_SERVLET_CONTEXT;
    }

    default String getGlobalEncoding() {
        return GlobalConfiguration.DEFAULT_GLOBAL_ENCODING;
    }

    default String getThymeleafPrefix() {
        return GlobalConfiguration.DEFAULT_THYMELEAF_PREFIX;
    }

    default String getThymeleafSuffix() {
        return GlobalConfiguration.DEFAULT_THYMELEAF_SUFFIX;
    }

    default String getStaticPrefix() {
        return GlobalConfiguration.DEFAULT_STATIC_PREFIX;
    }

    default String getBanner() {
        return "";
    }

}
