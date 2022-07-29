package top.huanyv.web.servlet;

import top.huanyv.utils.PropertiesUtil;
import top.huanyv.web.config.WebGlobalConfig;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author admin
 * @date 2022/7/24 17:17
 */
public class WebApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

//        String configLocation = servletContext.getInitParameter("ConfigLocation");
//        Properties properties = PropertiesUtil.getProperties(configLocation);
//        String packages = properties.getProperty("component.scan");

        String scanPackages = servletContext.getInitParameter("ScanPackages");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(scanPackages.split(","));

        servletContext.setAttribute(WebGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME, applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
