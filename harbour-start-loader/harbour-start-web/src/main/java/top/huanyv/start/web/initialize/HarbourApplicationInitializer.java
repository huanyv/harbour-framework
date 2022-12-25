package top.huanyv.start.web.initialize;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CommandLineArguments;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author huanyv
 * @date 2022/12/24 16:37
 */
public abstract class HarbourApplicationInitializer implements WebStartupInitializer {

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        Class<?> mainClass = run();
        AppArguments appArguments = new AppArguments();
        appArguments.load(new CommandLineArguments());

        HarbourApplication application = new HarbourApplication(mainClass);
        application.setAppArguments(appArguments);

        ApplicationContext applicationContext = application.createApplicationContext();

        RouterServlet routerServlet = new RouterServlet(applicationContext);
        ServletRegistration.Dynamic router = ctx.addServlet(WebMvcGlobalConfig.ROUTER_SERVLET_NAME, routerServlet);
        router.setLoadOnStartup(1);
        router.setMultipartConfig(new MultipartConfigElement("",
                appArguments.getLong("server.maxFileSize", "1048576"),
                appArguments.getLong("server.maxRequestSize", "10485760"), 0));
        router.addMapping("/");
    }

    public abstract Class<?> run();
}
