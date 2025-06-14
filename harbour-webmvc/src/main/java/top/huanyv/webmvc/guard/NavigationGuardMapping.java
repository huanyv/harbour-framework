package top.huanyv.webmvc.guard;

import top.huanyv.bean.utils.Ordered;
import top.huanyv.webmvc.config.WebMvcConstants;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public class NavigationGuardMapping implements Ordered {

    // 请求地址
    private String[] urlPatterns;

    // 排除的地址
    private String[] excludeUrl;

    // 路由守卫
    private NavigationGuard navigationGuard;

    // 顺序
    private int order;

    /**
     * 是否是这个地址
     * @param path 地址
     * @return bool
     */
    public boolean hasUrlPatten(String path) {
        if (this.urlPatterns == null) {
            return false;
        }
        for (String pattern : this.urlPatterns) {
            if (WebMvcConstants.PATH_SEPARATOR.equals(path) && "/**".equals(pattern)) {
                return true;
            }
            if (WebMvcConstants.ANT_PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否排除了这个地址
     * @param path 地址
     * @return bool
     */
    public boolean isExclude(String path) {
        if (this.excludeUrl == null) {
            return false;
        }
        for (String pattern : this.excludeUrl) {
            if (WebMvcConstants.ANT_PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    public String[] getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String[] urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String[] getExcludeUrl() {
        return excludeUrl;
    }

    public void setExcludeUrl(String[] excludeUrl) {
        this.excludeUrl = excludeUrl;
    }

    public NavigationGuard getNavigationGuard() {
        return navigationGuard;
    }

    public void setNavigationGuard(NavigationGuard navigationGuard) {
        this.navigationGuard = navigationGuard;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public NavigationGuardMapping setOrder(int order) {
        this.order = order;
        return this;
    }

    public NavigationGuardMapping addUrlPattern(String... urlPatterns) {
        this.urlPatterns = urlPatterns;
        return this;
    }
    public NavigationGuardMapping excludeUrlPattern(String... excludeUrl) {
        this.excludeUrl = excludeUrl;
        return this;
    }

}
