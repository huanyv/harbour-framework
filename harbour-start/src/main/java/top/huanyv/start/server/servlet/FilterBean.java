package top.huanyv.start.server.servlet;

import top.huanyv.start.server.NativeServletRegistry;
import top.huanyv.tools.utils.StringUtil;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/12/25 15:43
 */
public class FilterBean {

    private Filter filter;

    private String name;

    private Set<String> urlPatterns;

    private Map<String, String> initParameters;

    public FilterBean() {
        this.initParameters = new ConcurrentHashMap<>();
        this.urlPatterns = new HashSet<>();
    }

    public FilterBean(Filter filter) {
        this();
        setFilter(filter);
    }

    public FilterBean(String filterName, Filter filter) {
        this();
        this.filter = filter;
        this.name = filterName;
    }

    public FilterBean(Filter filter, String... urlPatterns) {
        this();
        setFilter(filter);
        this.urlPatterns.addAll(Arrays.asList(urlPatterns));
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        setName(StringUtil.firstLetterLower(filter.getClass().getSimpleName()));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUrlPatterns(String... urlPatterns) {
        this.urlPatterns.addAll(Arrays.asList(urlPatterns));
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public void addInitParameter(String name, String value) {
        this.initParameters.put(name, value);
    }

    public Filter getFilter() {
        return filter;
    }

    public String getName() {
        return name;
    }

    public void populateFilterRegistration(FilterRegistration.Dynamic filterRegistration) {
        filterRegistration.setInitParameters(this.initParameters);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),
                true, this.urlPatterns.toArray(new String[0]));
    }

    public void addRegistration(NativeServletRegistry servletRegistry) {
        FilterRegistration.Dynamic filterRegistration = servletRegistry.addFilter(this.name, this.filter);
        populateFilterRegistration(filterRegistration);
    }

}
