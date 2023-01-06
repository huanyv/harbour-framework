package top.huanyv.start.web.initialize;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CliArguments;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.start.web.servlet.ServletContextRegistry;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.*;

/**
 * @author huanyv
 * @date 2022/12/24 16:37
 */
public abstract class HarbourApplicationInitializer implements WebStartupInitializer {

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        Class<?> mainClass = run();
        AppArguments appArguments = new AppArguments(new CliArguments());

        // 创建应用
        HarbourApplication application = new HarbourApplication(mainClass);
        application.setAppArguments(appArguments);

        // 创建容器
        ApplicationContext applicationContext = application.createApplicationContext();

        // 注册前端控制器
        RouterServlet routerServlet = new RouterServlet(applicationContext);
        ServletRegistration.Dynamic router = ctx.addServlet(WebMvcGlobalConfig.ROUTER_SERVLET_NAME, routerServlet);
        router.setLoadOnStartup(1);
        router.setMultipartConfig(new MultipartConfigElement("",
                appArguments.getLong("server.maxFileSize", 1048576L),
                appArguments.getLong("server.maxRequestSize", 10485760L), 0));
        router.addMapping("/");

        // 注册原生的 Servlet
        ServletContextRegistry servletContextRegistry = new ServletContextRegistry(ctx);
        application.registerServlet(applicationContext, servletContextRegistry);
    }

    /**
     * 重写此方法，返回值应是IOC要扫描的包中的一个类，即主方法类
     *
     * @return {@link Class}<{@link ?}>
     */
    public abstract Class<?> run();
}
