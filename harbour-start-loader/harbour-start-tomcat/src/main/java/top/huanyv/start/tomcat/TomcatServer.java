package top.huanyv.start.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.ApplicationFilterRegistration;
import org.apache.catalina.core.ApplicationServletRegistration;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import top.huanyv.start.server.WebServer;

import javax.servlet.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

/**
 * @author huanyv
 * @date 2022/12/17 14:36
 */
public class TomcatServer implements WebServer {

    public TomcatServer(String contextPath) {
        setContextPath(contextPath);
    }

    /**
     * Tomcat容器
     */
    private final Tomcat tomcat = new Tomcat();

    /**
     * 上下文对象
     */
    private Context context;

    /**
     * 端口号
     */
    private int port;

    private String contextPath;

    private String uriEncoding;

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        Wrapper wrapper = Tomcat.addServlet(context, servletName, servlet);
        return new ApplicationServletRegistration(wrapper, context);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(filter);
        filterDef.setFilterName(filterName);
        this.context.addFilterDef(filterDef);
        return new ApplicationFilterRegistration(filterDef, this.context);
    }

    @Override
    public void addListener(EventListener eventListener) {
        Object[] eventListeners = this.context.getApplicationEventListeners();
        if (eventListeners.length == 0) {
            this.context.setApplicationEventListeners(new Object[]{eventListener});
            return;
        }
        int newLen = eventListeners.length + 1;
        Object[] newListeners = Arrays.copyOf(eventListeners, newLen);
        newListeners[newLen] = eventListener;
        this.context.setApplicationEventListeners(newListeners);
    }

    @Override
    public void start() {
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setURIEncoding(uriEncoding);
        this.tomcat.getService().addConnector(connector);
        try {
            this.tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        try {
            this.tomcat.stop();
            this.tomcat.destroy();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
        this.context = this.tomcat.addContext(contextPath, System.getProperty("java.io.tmpdir"));
    }

    public void setUriEncoding(String uriEncoding) {
        this.uriEncoding = uriEncoding;
    }
}
