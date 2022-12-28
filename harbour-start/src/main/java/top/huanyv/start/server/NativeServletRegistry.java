package top.huanyv.start.server;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.EventListener;

/**
 * @author huanyv
 * @date 2022/12/27 14:36
 */
public interface NativeServletRegistry {
    ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet);

    FilterRegistration.Dynamic addFilter(String filterName, Filter filter);

    void addListener(EventListener eventListener);
}
