package top.huanyv.web.servlet;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.web.config.DefaultWebConfigurer;
import top.huanyv.web.config.WebConfigurer;
import top.huanyv.web.config.WebMvcGlobalConfig;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;
import top.huanyv.web.core.RequestHandlerRegistry;
import top.huanyv.web.exception.ExceptionHandler;
import top.huanyv.web.guard.NavigationGuardMapping;
import top.huanyv.web.view.ResourceHandler;
import top.huanyv.web.view.ViewResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/29 9:11
 */
public abstract class TemplateServlet extends HttpServlet {
    /**
     * 请求注册容器
     */
    protected RequestHandlerRegistry requestRegistry;

    /**
     * 视图解析器
     */
    protected ViewResolver viewResolver;

    /**
     * 资源解析器
     */
    protected ResourceHandler resourceHandler;

    /**
     * 路由守卫链
     */
    protected List<NavigationGuardMapping> guardMappings = new ArrayList<>();

    /**
     * 异常处理器
     */
    protected ExceptionHandler exceptionHandler;

    /**
     * 配置类
     */
    protected WebConfigurer webConfigurer;

    @Override
    public void init() throws ServletException {
        requestRegistry = RequestHandlerRegistry.single();
        resourceHandler = new ResourceHandler();

        // 获取IOC容器
        ServletContext servletContext = getServletContext();
        ApplicationContext applicationContext
                = (ApplicationContext)servletContext.getAttribute(WebMvcGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME);

        // 获取配置类
        webConfigurer = applicationContext.getBean(WebConfigurer.class);
        if (webConfigurer == null) {
            // 如果容器中没有配置类，用默认的
            webConfigurer = new DefaultWebConfigurer();
        }

        // 初始化路由地址映射信息
        initRouting(applicationContext);
        // 初始化异常处理器
        initExceptionHandler(applicationContext);
        // 初始化视图解析
        initViewResolver(applicationContext);
        // 初始化资源映射
        initResourceMapping(applicationContext);
        // 初始化路由守卫
        initNavigationGuard(applicationContext);
    }

    abstract void initRouting(ApplicationContext applicationContext);

    abstract void initExceptionHandler(ApplicationContext applicationContext);

    abstract void initViewResolver(ApplicationContext applicationContext);

    abstract void initResourceMapping(ApplicationContext applicationContext);

    abstract void initNavigationGuard(ApplicationContext applicationContext);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        HttpRequest httpRequest = null;
        HttpResponse httpResponse = null;
        try {
            req.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            httpRequest = new HttpRequest(req, resp);
            httpRequest.setViewResolver(this.viewResolver);
            httpResponse = new HttpResponse(req, resp);
            // 路由分发
            doRouting(req, resp);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            if (throwable instanceof Exception) {
                Exception targetEx = (Exception) throwable;
                doException(httpRequest, httpResponse, targetEx);
            }
        } catch (Exception e) {
            doException(httpRequest, httpResponse, e);
        }
    }

    abstract void doRouting(HttpServletRequest req, HttpServletResponse resp) throws Exception;


    abstract void doException(HttpRequest req, HttpResponse resp, Exception ex);

}