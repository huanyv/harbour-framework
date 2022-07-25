package top.huanyv.web.config;

import cn.hutool.core.io.resource.ResourceUtil;
import top.huanyv.web.utils.SystemConstants;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author admin
 * @date 2022/7/6 8:56
 */
public interface WebConfigurer {

    default void addViewController(ViewControllerRegistry registry) {

    }



}
