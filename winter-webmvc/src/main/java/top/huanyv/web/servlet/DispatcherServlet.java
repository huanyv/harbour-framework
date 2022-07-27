package top.huanyv.web.servlet;

import top.huanyv.web.anno.Guard;
import top.huanyv.web.anno.Order;
import top.huanyv.web.anno.RequestPath;
import top.huanyv.web.config.*;
import top.huanyv.web.core.*;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.utils.WebUtil;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.guard.NavigationGuard;
import top.huanyv.web.guard.NavigationGuardChain;
import top.huanyv.web.guard.NavigationGuardMapping;
import top.huanyv.web.utils.SystemConstants;
import top.huanyv.web.view.ResourceHandler;
import top.huanyv.web.view.ViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请求分发器
 * 所有请求都会到这个servlet
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 请求注册容器
     */
    private RequestHandlerRegistry requestRegistry;

    /**
     * 视图解析器
     */
    private ViewResolver viewResolver;

    /**
     * 资源解析器
     */
    private ResourceHandler resourceHandler;

    private List<NavigationGuardMapping> guardMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        requestRegistry = RequestHandlerRegistry.single();
        resourceHandler = new ResourceHandler();

        // 获取IOC容器
        ServletContext servletContext = getServletContext();
        ApplicationContext applicationContext
                = (ApplicationContext)servletContext.getAttribute(WebGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME);

        // 遍历所有的bean，找到controller
        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            RequestPath requestPath = bean.getClass().getAnnotation(RequestPath.class);
            if (requestPath != null) {
                // 基路由
                String path = requestPath.value();

                for (Method method : bean.getClass().getDeclaredMethods()) {
                    RequestPath methodRequestPath = method.getAnnotation(RequestPath.class);
                    if (methodRequestPath != null) {
                        // 拼接上子路由
                        path += methodRequestPath.value();
                        requestRegistry.register(path, methodRequestPath.method(), bean, method);
                    }
                }
            }
        }
        // 获取配置类
        WebConfigurer webConfigurer = applicationContext.getBean(WebConfigurer.class);
        if (webConfigurer == null) {
            // 如果容器中没有配置类，用默认的
            webConfigurer = new DefaultWebConfigurer();
        }

        // 视图解析器配置
        this.viewResolver = applicationContext.getBean(ViewResolver.class);

        if (this.viewResolver != null) {
            // 视图控制器配置
            ViewControllerRegistry viewControllerRegistry = new ViewControllerRegistry();
            webConfigurer.addViewController(viewControllerRegistry);
            for (Map.Entry<String, String> entry : viewControllerRegistry.getViewController().entrySet()) {
                this.requestRegistry.register(entry.getKey(), (req, resp) -> req.view(entry.getValue()));
            }
        }

        // 静态资源配置
        ResourceMappingRegistry resourceMappingRegistry = new ResourceMappingRegistry();
        webConfigurer.addResourceMapping(resourceMappingRegistry);
        for (Map.Entry<String, String> entry : resourceMappingRegistry.getResourceMapping().entrySet()) {
            this.resourceHandler.add(entry.getKey(), entry.getValue());
        }

        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
            Object bean = applicationContext.getBean(beanDefinitionName);
            if (bean instanceof NavigationGuard) {
                NavigationGuard navigationGuard = (NavigationGuard) bean;
                Guard guard = bean.getClass().getAnnotation(Guard.class);
                if (guard != null) {
                    Order order = bean.getClass().getAnnotation(Order.class);
                    String[] urlPatterns = guard.value();
                    String[] exclude = guard.exclude();
                    navigationGuardMapping.setNavigationGuard(navigationGuard);
                    navigationGuardMapping.setUrlPatterns(urlPatterns);
                    navigationGuardMapping.setExcludeUrl(exclude);
                    if (order != null) {
                        navigationGuardMapping.setOrder(order.value());
                    } else {
                        navigationGuardMapping.setOrder(0);
                    }
                    this.guardMappings.add(navigationGuardMapping);
                }
            }
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = WebUtil.getRequestURI(req);

        HttpRequest httpRequest = new HttpRequest(req, resp);
        httpRequest.setViewResolver(this.viewResolver);

        NavigationGuardChain navigationGuardChain = getNavigationGuardChain(req);
        boolean handleBefore = navigationGuardChain.handleBefore(httpRequest, new HttpResponse(req, resp));
        if (!handleBefore) {
            return;
        }

        // 处理请求
        if (!this.requestRegistry.containsRequest(uri)) {
            // 处理静态资源
            if (resourceHandler.hasResource(req)) {
                resourceHandler.handle(req, resp);
                return;
            }
            resp.sendError(404,"resources not found.");
            return;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod().toUpperCase());
        // 获取当前uri的对应请求处理器映射
        RequestMapping mapping = this.requestRegistry.getMapping(uri);
        // 获取当前请求方式的处理
        RequestHandler requestHandler = mapping.getRequestHandler(requestMethod);

        // 设置pathVar
        mapping.parsePathVars(uri);

        // 判断处理器是否存在
        if (requestHandler != null) {
            // 处理请求
            requestHandler.handle(httpRequest, new HttpResponse(req, resp));
        } else {
            // 这个请求方式没有注册
            resp.sendError(405, "request method not exists.");
        }

        navigationGuardChain.handleAfter(new HttpRequest(req, resp), new HttpResponse(req, resp));

    }


    public NavigationGuardChain getNavigationGuardChain(HttpServletRequest request) {
        String uri = WebUtil.getRequestURI(request);
        NavigationGuardChain navigationGuardChain = new NavigationGuardChain();
        if (SystemConstants.PATH_SEPARATOR.equals(uri)) {
            navigationGuardChain.setNavigationGuards(this.guardMappings);
            return navigationGuardChain;
        }
        List<NavigationGuardMapping> navigationGuardMappings = this.guardMappings.stream()
                .filter(navigationGuardMapping -> navigationGuardMapping.hasUrlPatten(uri)
                        && !navigationGuardMapping.isExclude(uri))
                .collect(Collectors.toList());
        navigationGuardChain.setNavigationGuards(navigationGuardMappings);
        return navigationGuardChain;
    }


}
