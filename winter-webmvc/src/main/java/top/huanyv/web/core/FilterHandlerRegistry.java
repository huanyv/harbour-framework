package top.huanyv.web.core;

import top.huanyv.web.interfaces.FilterHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/6 10:03
 */
public class FilterHandlerRegistry {

    // 单例
    private FilterHandlerRegistry() {}
    private static class SingletonHolder {
        public static final FilterHandlerRegistry REGISTRY = new FilterHandlerRegistry();
    }
    public static FilterHandlerRegistry single() {
        return SingletonHolder.REGISTRY;
    }

    private List<FilterMapping> registry = new ArrayList<>();

    public void register(String urlPattern, FilterHandler filterHandler) {
        this.registry.add(new FilterMapping(urlPattern, filterHandler));
    }

}
