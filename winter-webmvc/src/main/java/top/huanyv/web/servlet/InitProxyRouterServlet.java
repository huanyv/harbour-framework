package top.huanyv.web.servlet;

import top.huanyv.ioc.anno.Order;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.exception.NoSuchBeanDefinitionException;
import top.huanyv.ioc.utils.AopUtil;
import top.huanyv.ioc.utils.BeanFactoryUtil;
import top.huanyv.web.anno.*;
import top.huanyv.web.config.*;
import top.huanyv.web.core.*;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.exception.DefaultExceptionHandler;
import top.huanyv.web.exception.ExceptionHandler;
import top.huanyv.web.guard.NavigationGuard;
import top.huanyv.web.guard.NavigationGuardMapping;
import top.huanyv.web.interfaces.ServletHandler;
import top.huanyv.web.view.ViewResolver;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/29 9:22
 */
public abstract class InitProxyRouterServlet extends TemplateServlet {
    @Override
    void initRouting(ApplicationContext applicationContext) {
        // 遍历所有的bean，找到所有的路由
        for (Object bean : BeanFactoryUtil.getBeans(applicationContext)) {
            Class<?> targetClass = AopUtil.getTargetClass(bean);
            Route route = targetClass.getAnnotation(Route.class);
            if (route != null) {
                // 遍历方法
                for (Method method : targetClass.getDeclaredMethods()) {
                    // 基路由
                    String basePath = route.value();
                    RequestHandler requestHandler = new MethodRequestHandler(targetClass, method);
                    Route methodRoute = method.getAnnotation(Route.class);
                    if (methodRoute != null) {
                        // 拼接上子路由
                        String path = basePath + methodRoute.value();
                        requestRegistry.registerHandler(path, requestHandler);
                        continue;
                    }
                    Get get = method.getAnnotation(Get.class);
                    if (get != null) {
                        String path = basePath + get.value();
                        requestRegistry.registerHandler(path, RequestMethod.GET, requestHandler);
                        continue;
                    }
                    Post post = method.getAnnotation(Post.class);
                    if (post != null) {
                        String path = basePath + post.value();
                        requestRegistry.registerHandler(path, RequestMethod.POST, requestHandler);
                        continue;
                    }
                    Put put = method.getAnnotation(Put.class);
                    if (put != null) {
                        String path = basePath + put.value();
                        requestRegistry.registerHandler(path, RequestMethod.PUT, requestHandler);
                        continue;
                    }
                    Delete delete = method.getAnnotation(Delete.class);
                    if (delete != null) {
                        String path = basePath + delete.value();
                        requestRegistry.registerHandler(path, RequestMethod.DELETE, requestHandler);
                        continue;
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
        try {
            this.exceptionHandler = applicationContext.getBean(ExceptionHandler.class);
        } catch (NoSuchBeanDefinitionException e) {
            // 如果容器中没有，使用默认的
            this.exceptionHandler = new DefaultExceptionHandler();
        }
    }

    @Override
    void initViewResolver(ApplicationContext applicationContext) {
        // 视图解析器配置
        try {
            this.viewResolver = applicationContext.getBean(ViewResolver.class);
            // 视图控制器配置
            if (this.viewResolver != null) {
                ViewControllerRegistry viewControllerRegistry = new ViewControllerRegistry();
                webConfigurer.addViewController(viewControllerRegistry);
                for (Map.Entry<String, String> entry : viewControllerRegistry.getViewController().entrySet()) {
                    this.requestRegistry.register(entry.getKey(), (req, resp) -> req.view(entry.getValue()));
                }
            }
        } catch (NoSuchBeanDefinitionException e) {
        }

    }

    @Override
    void initResourceMapping(ApplicationContext applicationContext) {
        // 静态资源配置
        ResourceMappingRegistry resourceMappingRegistry = new ResourceMappingRegistry();
        webConfigurer.addResourceMapping(resourceMappingRegistry);
        this.resourceHandler.setResourceMappingRegistry(resourceMappingRegistry);
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
            Class<?> targetClass = AopUtil.getTargetClass(navigationGuard);
            Guard guard = targetClass.getAnnotation(Guard.class);
            if (guard != null) {
                // 获得顺序
                Order order = targetClass.getAnnotation(Order.class);
                // 获得匹配路径
                String[] urlPatterns = guard.value();
                // 获得排序路径
                String[] exclude = guard.exclude();

                NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
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
