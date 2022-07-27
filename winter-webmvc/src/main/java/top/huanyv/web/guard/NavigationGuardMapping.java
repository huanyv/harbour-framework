package top.huanyv.web.guard;

import jdk.nashorn.internal.ir.IfNode;
import top.huanyv.utils.AntPathMatcher;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.util.Arrays;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public class NavigationGuardMapping {

    private String[] urlPatterns;

    private String[] excludeUrl;

    private NavigationGuard navigationGuard;

    private int order;

    public boolean hasUrlPatten(String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : this.urlPatterns) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExclude(String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String pattern : this.excludeUrl) {
            if (antPathMatcher.match(pattern, path)) {
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
