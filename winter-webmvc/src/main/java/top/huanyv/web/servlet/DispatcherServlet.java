package top.huanyv.web.servlet;

import top.huanyv.ioc.utils.BeanFactoryUtil;
import top.huanyv.web.anno.ExceptionPoint;
import top.huanyv.web.anno.Guard;
import top.huanyv.web.anno.Order;
import top.huanyv.web.anno.RequestPath;
import top.huanyv.web.config.*;
import top.huanyv.web.core.*;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.utils.WebUtil;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.exception.DefaultExceptionHandler;
import top.huanyv.web.exception.ExceptionHandler;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    /**
     * 路由守卫链
     */
    private List<NavigationGuardMapping> guardMappings = new ArrayList<>();

    /**
     * 异常处理器
     */
    private ExceptionHandler exceptionHandler;

    @Override
    public void init() throws ServletException {
        requestRegistry = RequestHandlerRegistry.single();
        resourceHandler = new ResourceHandler();

        // 获取IOC容器
        ServletContext servletContext = getServletContext();
        ApplicationContext applicationContext
                = (ApplicationContext)servletContext.getAttribute(WebGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME);

        // 遍历所有的bean，找到controller
        for (Object bean : BeanFactoryUtil.getBeans(applicationContext)) {
            RequestPath requestPath = bean.getClass().getAnnotation(RequestPath.class);
            if (requestPath != null) {
                // 遍历方法
                for (Method method : bean.getClass().getDeclaredMethods()) {
                    // 基路由
                    String path = requestPath.value();
                    RequestPath methodRequestPath = method.getAnnotation(RequestPath.class);
                    if (methodRequestPath != null) {
                        // 拼接上子路由
                        path += methodRequestPath.value();
                        requestRegistry.register(path, methodRequestPath.method(), bean, method);
                    }
                }
            }
        }

        this.exceptionHandler = applicationContext.getBean(ExceptionHandler.class);
        if (this.exceptionHandler == null) {
            this.exceptionHandler = new DefaultExceptionHandler();
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

        // 配置路由守卫
        NavigationGuardRegistry navigationGuardRegistry = new NavigationGuardRegistry();
        webConfigurer.configNavigationRegistry(navigationGuardRegistry);
        this.guardMappings.addAll(navigationGuardRegistry.getConfigNavigationGuards());

        // 扫描路由守卫
        for (Object bean : BeanFactoryUtil.getBeans(applicationContext)) {
            // 如果是路由守卫
            if (bean instanceof NavigationGuard) {
                NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
                NavigationGuard navigationGuard = (NavigationGuard) bean;
                Guard guard = bean.getClass().getAnnotation(Guard.class);
                if (guard != null) {
                    // 获得顺序
                    Order order = bean.getClass().getAnnotation(Order.class);
                    // 获得匹配路径
                    String[] urlPatterns = guard.value();
                    // 获得排序路径
                    String[] exclude = guard.exclude();
                    navigationGuardMapping.setNavigationGuard(navigationGuard);
                    navigationGuardMapping.setUrlPatterns(urlPatterns);
                    navigationGuardMapping.setExcludeUrl(exclude);
                    if (order != null) {
                        navigationGuardMapping.setOrder(order.value());
                    } else {
                        navigationGuardMapping.setOrder(guard.order());
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
        HttpResponse httpResponse = new HttpResponse(req, resp);

        // 获取路由守卫执行链
        NavigationGuardChain navigationGuardChain = getNavigationGuardChain(req);
        // 路由守卫前置操作
        boolean handleBefore = navigationGuardChain.handleBefore(httpRequest, httpResponse);
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
            try {
                requestHandler.handle(httpRequest, httpResponse);
            } catch (InvocationTargetException e) {
                // TODO 异常处理器转发
                Exception targetException = (Exception) e.getTargetException();
                Method exceptionMethod = null;
                for (Method method : exceptionHandler.getClass().getDeclaredMethods()) {
                    ExceptionPoint exceptionPoint = method.getAnnotation(ExceptionPoint.class);
                    if (exceptionPoint != null) {
                        for (Class<? extends Throwable> clazz : exceptionPoint.value()) {
                            if (clazz.isInstance(targetException)) {
                                exceptionMethod = method;
                            }
                        }
                    }
                }

                if (exceptionMethod == null) {
                    this.exceptionHandler.handle(httpRequest, httpResponse, targetException);
                } else {
                    try {
                        exceptionMethod.invoke(this.exceptionHandler, httpRequest, httpResponse, targetException);
                    } catch (IllegalAccessException | InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                }

                return;
//                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            // 这个请求方式没有注册
            resp.sendError(405, "request method not exists.");
        }

        // 路由守卫 后置操作
        navigationGuardChain.handleAfter(httpRequest, httpResponse);

    }


    public NavigationGuardChain getNavigationGuardChain(HttpServletRequest request) {
        // 请求uri
        String uri = WebUtil.getRequestURI(request);
        NavigationGuardChain navigationGuardChain = new NavigationGuardChain();

        List<NavigationGuardMapping> navigationGuardMappings = this.guardMappings.stream()
                .filter(navigationGuardMapping -> navigationGuardMapping.hasUrlPatten(uri)
                        && !navigationGuardMapping.isExclude(uri))
                .sorted((o1, o2) -> {
                    // 排序，如果序号一样，按照类名顺序
                    if (o1.getOrder() == o2.getOrder()) {
                        return o1.getNavigationGuard().getClass().getName()
                                .compareTo(o2.getNavigationGuard().getClass().getName());
                    }
                    return o1.getOrder() - o2.getOrder();
                }).collect(Collectors.toList());
        navigationGuardChain.setNavigationGuards(navigationGuardMappings);
        return navigationGuardChain;
    }


}
