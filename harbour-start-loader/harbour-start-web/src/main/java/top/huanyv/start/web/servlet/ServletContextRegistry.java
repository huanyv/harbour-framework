package top.huanyv.start.web.servlet;

import top.huanyv.start.server.NativeServletRegistry;

import javax.servlet.*;
import java.util.EventListener;

/**
 * 通过ServletContext注册原生组件
 *
 * @author huanyv
 * @date 2022/12/27 14:41
 */
public class ServletContextRegistry implements NativeServletRegistry {

    private ServletContext servletContext;

    public ServletContextRegistry(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return servletContext.addServlet(servletName, servlet);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return servletContext.addFilter(filterName, filter);
    }

    @Override
    public void addListener(EventListener eventListener) {
        servletContext.addListener(eventListener);
    }

}
