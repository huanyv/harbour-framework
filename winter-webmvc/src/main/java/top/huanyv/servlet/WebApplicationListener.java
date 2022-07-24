package top.huanyv.servlet;

import top.huanyv.config.WebGlobalConfig;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author admin
 * @date 2022/7/24 17:17
 */
public class WebApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String configLocation = servletContext.getInitParameter("ConfigLocation");

        String packages = WebGlobalConfig.getConfig("component.scan", null);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(packages.split(","));

        servletContext.setAttribute(WebGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME, applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
