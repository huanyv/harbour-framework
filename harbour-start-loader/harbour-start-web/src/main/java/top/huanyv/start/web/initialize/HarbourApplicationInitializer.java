package top.huanyv.start.web.initialize;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CliArguments;
import top.huanyv.start.core.HarbourApplication;
import top.huanyv.start.web.servlet.ServletContextRegistry;
import top.huanyv.tools.utils.FileUtil;
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
        // 文件上传配置，默认最大单文件大小1MB，最大请求大小10MB
        long maxFileSize = FileUtil.parseSize(appArguments.get("server.maxFileSize", "1MB"));
        long maxRequestSize = FileUtil.parseSize(appArguments.get("server.maxRequestSize", "10MB"));
        System.out.println("maxFileSize = " + maxFileSize);
        System.out.println("maxRequestSize = " + maxRequestSize);
        router.setMultipartConfig(new MultipartConfigElement("", maxFileSize, maxRequestSize, 0));
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
