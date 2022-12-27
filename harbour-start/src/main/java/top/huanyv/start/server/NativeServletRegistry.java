package top.huanyv.start.server;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.server.servlet.FilterBean;
import top.huanyv.start.server.servlet.ServletBean;
import top.huanyv.start.server.servlet.ServletListenerBean;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.util.EventListener;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/12/27 14:36
 */
public interface NativeServletRegistry {
    ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet);

    FilterRegistration.Dynamic addFilter(String filterName, Filter filter);

    void addListener(EventListener eventListener);

    static void register(ApplicationContext applicationContext, NativeServletRegistry nativeServletRegistry) {
        List<ServletBean> servletBeans = BeanFactoryUtil.getBeansByType(applicationContext, ServletBean.class);
        for (ServletBean servletBean : servletBeans) {
            servletBean.addRegistration(nativeServletRegistry);
        }

        List<FilterBean> filterBeans = BeanFactoryUtil.getBeansByType(applicationContext, FilterBean.class);
        for (FilterBean filterBean : filterBeans) {
            filterBean.addRegistration(nativeServletRegistry);
        }

        List<ServletListenerBean> listenerBeans = BeanFactoryUtil.getBeansByType(applicationContext, ServletListenerBean.class);
        for (ServletListenerBean listenerBean : listenerBeans) {
            listenerBean.addRegistration(nativeServletRegistry);
        }
    }

}
