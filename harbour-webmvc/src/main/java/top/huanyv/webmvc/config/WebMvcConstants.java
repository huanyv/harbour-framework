package top.huanyv.webmvc.config;

import top.huanyv.bean.utils.AntPathMatcher;

/**
 * @author admin
 * @date 2022/7/6 9:05
 */
public class WebMvcConstants {

    public static final String WEB_BEAN_SCAN_PACKAGES = "ScanPackages";

    public static final String ROUTER_SERVLET_NAME = "routerServlet";

    public static final String PATH_SEPARATOR = "/";

    public static final String WEB_APPLICATION_CONTEXT_ATTR_NAME = "webApplicationContext";

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

}
