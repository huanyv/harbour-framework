package top.huanyv.core;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import top.huanyv.interfaces.FilterHandler;
import top.huanyv.servlet.GlobalFilter;
import top.huanyv.utils.StringUtil;

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

    public void toContext(Context context) {
        for (FilterMapping mapping : this.registry) {
            String urlPattern = mapping.getUrlPattern();
            FilterHandler filterHandler = mapping.getFilterHandler();

            GlobalFilter globalFilter = new GlobalFilter();
            globalFilter.setFilterHandler(filterHandler);

            String uuid = StringUtil.getUUID();

            FilterDef filterDef = new FilterDef();
            filterDef.setFilter(globalFilter);
            filterDef.setFilterName(uuid);
            context.addFilterDef(filterDef);

            FilterMap filterMap = new FilterMap();
            filterMap.setFilterName(uuid);
            filterMap.addURLPattern(urlPattern);
            context.addFilterMap(filterMap);
        }
    }
}
