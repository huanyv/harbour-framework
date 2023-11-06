package top.huanyv.start.server.servlet;

import top.huanyv.start.server.NativeServletRegistry;
import top.huanyv.bean.utils.StringUtil;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/12/25 15:14
 */
public class ServletBean implements Registration {

    private Servlet servlet;

    private String name;

    private Set<String> urlMappings;

    private MultipartConfigElement multipartConfigElement;

    private Map<String, String> initParameters;

    private int loadOnStartup = -1;

    public ServletBean() {
        this.urlMappings = new HashSet<>();
        this.initParameters = new ConcurrentHashMap<>();
    }

    public ServletBean(Servlet servlet) {
        this();
        setServlet(servlet);
    }

    public ServletBean(String servletName, Servlet servlet) {
        this();
        setServlet(servlet);
        setName(servletName);
    }

    public ServletBean(Servlet servlet, String... urlMappings) {
        this();
        setServlet(servlet);
        this.urlMappings.addAll(Arrays.asList(urlMappings));
    }

    public void addMappings(String... urlMappings) {
        this.urlMappings.addAll(Arrays.asList(urlMappings));
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
        this.name = StringUtil.firstLetterLower(this.servlet.getClass().getSimpleName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        this.multipartConfigElement = multipartConfig;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public void addInitParameter(String name, String value) {
        this.initParameters.put(name, value);
    }

    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public String getName() {
        return name;
    }

    public void populateServletRegistration(ServletRegistration.Dynamic dynamic) {
        dynamic.addMapping(this.urlMappings.toArray(new String[0]));
        dynamic.setLoadOnStartup(this.loadOnStartup);
        dynamic.setMultipartConfig(this.multipartConfigElement);
        dynamic.setInitParameters(this.initParameters);
    }

    @Override
    public void addRegistration(NativeServletRegistry servletRegistry) {
        ServletRegistration.Dynamic servletRegistration = servletRegistry.addServlet(this.name, this.servlet);
        populateServletRegistration(servletRegistration);
    }
}
