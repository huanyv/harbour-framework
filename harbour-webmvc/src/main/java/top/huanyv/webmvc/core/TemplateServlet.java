package top.huanyv.webmvc.core;

import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.ReflectUtil;
import top.huanyv.webmvc.config.DefaultWebConfigurer;
import top.huanyv.webmvc.config.WebConfigurer;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.exception.ExceptionHandler;
import top.huanyv.webmvc.guard.NavigationGuardMapping;
import top.huanyv.webmvc.resource.ResourceHandler;
import top.huanyv.webmvc.utils.ServletHolder;
import top.huanyv.webmvc.view.ViewResolver;

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
     * IOC容器
     */
    protected ApplicationContext applicationContext;

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

        ServletContext servletContext = getServletContext();
        ServletHolder.setServletContext(servletContext);

        if (this.applicationContext == null) {
            String scanPackages = getServletConfig().getInitParameter(WebMvcGlobalConfig.WEB_BEAN_SCAN_PACKAGES);
            this.applicationContext = new AnnotationConfigApplicationContext(scanPackages.split(","));
        }

        // IOC容器存到上下文中
        servletContext.setAttribute(WebMvcGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME, applicationContext);

        // 获取配置类
        try {
            webConfigurer = applicationContext.getBean(WebConfigurer.class);
        } catch (NoSuchBeanDefinitionException e) {
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
        HttpRequest httpRequest = new HttpRequest(req, resp);
        httpRequest.setViewResolver(this.viewResolver);
        HttpResponse httpResponse = new HttpResponse(req, resp);

        ServletHolder.setRequest(httpRequest);
        ServletHolder.setResponse(httpResponse);
        try {
            req.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // 路由分发
            doRouting(httpRequest, httpResponse);
        } catch (InvocationTargetException e) {
            // 反射异常捕获，先获取反射中的具体异常
            Exception targetException = ReflectUtil.getTargetException(e);
            doException(httpRequest, httpResponse, targetException);
        } catch (Exception e) {
            // 其它异常捕获
            doException(httpRequest, httpResponse, e);
        } finally {
            ServletHolder.removeRequest();
            ServletHolder.removeResponse();
        }
    }

    abstract void doRouting(HttpRequest request, HttpResponse response) throws Exception;


    abstract void doException(HttpRequest req, HttpResponse resp, Exception ex);

}
