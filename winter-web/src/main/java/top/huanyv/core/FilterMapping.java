package top.huanyv.core;

import top.huanyv.interfaces.FilterHandler;

/**
 * @author admin
 * @date 2022/7/6 10:02
 */
public class FilterMapping {
    private String urlPattern;
    private FilterHandler filterHandler;

    public FilterMapping(String urlPattern, FilterHandler filterHandler) {
        this.urlPattern = urlPattern;
        this.filterHandler = filterHandler;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public FilterHandler getFilterHandler() {
        return filterHandler;
    }

}
