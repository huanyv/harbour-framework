package top.huanyv.web.servlet;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.utils.BeanFactoryUtil;
import top.huanyv.web.anno.*;
import top.huanyv.web.config.*;
import top.huanyv.web.core.*;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.exception.DefaultExceptionHandler;
import top.huanyv.web.exception.ExceptionHandler;
import top.huanyv.web.guard.NavigationGuard;
import top.huanyv.web.guard.NavigationGuardMapping;
import top.huanyv.web.view.ResourceMapping;
import top.huanyv.web.view.ViewResolver;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/29 9:22
 */
public abstract class InitRouterServlet extends TemplateServlet {
    @Override
    void initRouting(ApplicationContext applicationContext) {
        // 遍历所有的bean，找到所有的路由
        for (Object bean : BeanFactoryUtil.getBeans(applicationContext)) {
            Route route = bean.getClass().getAnnotation(Route.class);
            if (route != null) {
                // 遍历方法
                for (Method method : bean.getClass().getDeclaredMethods()) {
                    // 基路由
                    String basePath = route.value();
                    Route methodRoute = method.getAnnotation(Route.class);
                    if (methodRoute != null) {
                        // 拼接上子路由
                        String path = basePath + methodRoute.value();
                        requestRegistry.register(path, bean, method);
                    }
                    Get get = method.getAnnotation(Get.class);
                    if (get != null) {
                        String path = basePath + get.value();
                        requestRegistry.register(path, RequestMethod.GET, bean, method);
                    }
                    Post post = method.getAnnotation(Post.class);
                    if (post != null) {
                        String path = basePath + post.value();
                        requestRegistry.register(path, RequestMethod.POST, bean, method);
                    }
                    Put put = method.getAnnotation(Put.class);
                    if (put != null) {
                        String path = basePath + put.value();
                        requestRegistry.register(path, RequestMethod.PUT, bean, method);
                    }
                    Delete delete = method.getAnnotation(Delete.class);
                    if (delete != null) {
                        String path = basePath + delete.value();
                        requestRegistry.register(path, RequestMethod.DELETE, bean, method);
                    }
                }
            }
        }

        Routing routing = new DefaultRouting();
        for (RouteRegistry registry : BeanFactoryUtil.getBeansByType(applicationContext, RouteRegistry.class)) {
            registry.run(routing);
        }

    }

    @Override
    void initExceptionHandler(ApplicationContext applicationContext) {
        // 从容器中找到异常处理器
        this.exceptionHandler = applicationContext.getBean(ExceptionHandler.class);
        // 如果容器中没有，使用默认的
        if (this.exceptionHandler == null) {
            this.exceptionHandler = new DefaultExceptionHandler();
        }
    }

    @Override
    void initViewResolver(ApplicationContext applicationContext) {
        // 视图解析器配置
        this.viewResolver = applicationContext.getBean(ViewResolver.class);

        // 视图控制器配置
        if (this.viewResolver != null) {
            ViewControllerRegistry viewControllerRegistry = new ViewControllerRegistry();
            webConfigurer.addViewController(viewControllerRegistry);
            for (Map.Entry<String, String> entry : viewControllerRegistry.getViewController().entrySet()) {
                this.requestRegistry.register(entry.getKey(), (req, resp) -> req.view(entry.getValue()));
            }
        }
    }

    @Override
    void initResourceMapping(ApplicationContext applicationContext) {
        // 静态资源配置
        ResourceMappingRegistry resourceMappingRegistry = new ResourceMappingRegistry();
        webConfigurer.addResourceMapping(resourceMappingRegistry);
        this.resourceHandler.addMappings(resourceMappingRegistry.getResourceMappings());
    }

    @Override
    void initNavigationGuard(ApplicationContext applicationContext) {
        // 配置路由守卫
        NavigationGuardRegistry navigationGuardRegistry = new NavigationGuardRegistry();
        webConfigurer.configNavigationRegistry(navigationGuardRegistry);
        this.guardMappings.addAll(navigationGuardRegistry.getConfigNavigationGuards());

        // 配置跨域
        CorsRegistry corsRegistry = new CorsRegistry();
        webConfigurer.addCorsMappings(corsRegistry);
        for (CorsRegistryBean corsRegistryBean : corsRegistry.getCorsRegistryBeans()) {
            NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();

            CorsGuard corsGuard = new CorsGuard();
            corsGuard.setMaxAge(corsRegistryBean.getMaxAge());
            corsGuard.setAllowCredentials(corsRegistryBean.getAllowCredentials());
            corsGuard.setAllowedOriginPatterns(corsRegistryBean.getAllowedOriginPatterns());
            corsGuard.setAllowedHeaders(corsRegistryBean.getAllowedHeaders());
            corsGuard.setAllowedMethods(corsRegistryBean.getAllowedMethods());

            navigationGuardMapping.setNavigationGuard(corsGuard);
            navigationGuardMapping.setOrder(-10);
            navigationGuardMapping.addUrlPattern(corsRegistryBean.getUrlPattern());
            this.guardMappings.add(navigationGuardMapping);
        }

        // 扫描路由守卫
        for (NavigationGuard navigationGuard : BeanFactoryUtil.getBeansByType(applicationContext, NavigationGuard.class)) {
            NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
            Guard guard = navigationGuard.getClass().getAnnotation(Guard.class);
            if (guard != null) {
                // 获得顺序
                Order order = navigationGuard.getClass().getAnnotation(Order.class);
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
