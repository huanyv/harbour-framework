package top.huanyv.config;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import top.huanyv.utils.SystemConstants;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author admin
 * @date 2022/7/6 8:56
 */
public interface WebConfiguration {

    default int getServerPort() {
        return Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_SERVER_PORT, GlobalConfig.DEFAULT_SERVER_PORT));
    }

    default String getServerContext() {
        return GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_SERVER_CONTEXT, GlobalConfig.DEFAULT_SERVLET_CONTEXT);
    }

    default String getGlobalEncoding() {
        return GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_GLOBAL_ENCODING, GlobalConfig.DEFAULT_GLOBAL_ENCODING);
    }

    default String getThymeleafPrefix() {
        return GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_THYMELEAF_PREFIX, GlobalConfig.DEFAULT_THYMELEAF_PREFIX);
    }

    default String getThymeleafSuffix() {
        return GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_THYMELEAF_SUFFIX, GlobalConfig.DEFAULT_THYMELEAF_SUFFIX);
    }

    default String getStaticPrefix() {
        return GlobalConfig.getConfig(GlobalConfig.CONFIG_KEY_STATIC_PREFIX, GlobalConfig.DEFAULT_STATIC_PREFIX);
    }

    default String getBanner() {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(SystemConstants.BANNER_FILE_NAME);
        if (inputStream == null) {
            return SystemConstants.DEFAULT_BANNER;
        }
        return ResourceUtil.readStr(SystemConstants.BANNER_FILE_NAME, Charset.forName(getGlobalEncoding()));
    }

}
