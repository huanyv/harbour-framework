package top.huanyv.config;

import cn.hutool.core.io.resource.ResourceUtil;
import top.huanyv.utils.SystemConstants;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author admin
 * @date 2022/7/6 8:56
 */
public interface WebConfiguration {

    default int getServerPort() {
        return Integer.parseInt(WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_SERVER_PORT, WebGlobalConfig.DEFAULT_SERVER_PORT));
    }

    default String getServerContext() {
        return WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_SERVER_CONTEXT, WebGlobalConfig.DEFAULT_SERVLET_CONTEXT);
    }

    default String getGlobalEncoding() {
        return WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_GLOBAL_ENCODING, WebGlobalConfig.DEFAULT_GLOBAL_ENCODING);
    }

    default String getThymeleafPrefix() {
        return WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_THYMELEAF_PREFIX, WebGlobalConfig.DEFAULT_THYMELEAF_PREFIX);
    }

    default String getThymeleafSuffix() {
        return WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_THYMELEAF_SUFFIX, WebGlobalConfig.DEFAULT_THYMELEAF_SUFFIX);
    }

    default String getStaticPrefix() {
        return WebGlobalConfig.getConfig(WebGlobalConfig.CONFIG_KEY_STATIC_PREFIX, WebGlobalConfig.DEFAULT_STATIC_PREFIX);
    }

    default String getBanner() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(SystemConstants.BANNER_FILE_NAME);
        if (inputStream == null) {
            return SystemConstants.DEFAULT_BANNER;
        }
        return ResourceUtil.readStr(SystemConstants.BANNER_FILE_NAME, Charset.forName(getGlobalEncoding()));
    }

}
