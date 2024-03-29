package top.huanyv.webmvc.core;

import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.AopUtil;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.config.*;
import top.huanyv.webmvc.core.request.MethodRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.exception.DefaultExceptionHandler;
import top.huanyv.webmvc.exception.ExceptionHandler;
import top.huanyv.webmvc.guard.NavigationGuard;
import top.huanyv.webmvc.guard.NavigationGuardMapping;
import top.huanyv.webmvc.view.ViewResolver;

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
        for (Class<?> cls : BeanFactoryUtil.getBeanClasses(applicationContext)) {
            Route route = cls.getAnnotation(Route.class);
            if (route != null) {
                // 基路由
                String basePath = route.value();
                // 遍历方法
                for (Method method : cls.getDeclaredMethods()) {
                    RequestHandler requestHandler = new MethodRequestHandler(applicationContext, cls, method);
                    Route methodRoute = method.getAnnotation(Route.class);
                    if (methodRoute != null) {
                        // 拼接上子路由
                        String path = basePath + methodRoute.value();
                        for (RequestMethod requestMethod : methodRoute.method()) {
                            requestRegistry.registerHandler(path, requestMethod, requestHandler);
                        }
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

        for (RouteRegistry registry : BeanFactoryUtil.getBeansByType(applicationContext, RouteRegistry.class)) {
            registry.run(new DefaultRouting());
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
            ViewControllerRegistry viewControllerRegistry = new ViewControllerRegistry();
            webConfigurer.addViewController(viewControllerRegistry);
            for (Map.Entry<String, String> entry : viewControllerRegistry.getViewController().entrySet()) {
                this.requestRegistry.register(entry.getKey(), (req, resp) -> req.view(entry.getValue()));
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
            // 导航守卫映射
            NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();

            // 创建跨域的导航守卫
            CorsGuard corsGuard = new CorsGuard();
            corsGuard.setMaxAge(corsRegistryBean.getMaxAge());
            corsGuard.setAllowCredentials(corsRegistryBean.getAllowCredentials());
            corsGuard.setAllowedOriginPatterns(corsRegistryBean.getAllowedOriginPatterns());
            corsGuard.setAllowedHeaders(corsRegistryBean.getAllowedHeaders());
            corsGuard.setAllowedMethods(corsRegistryBean.getAllowedMethods());

            // 设置映射参数
            navigationGuardMapping.setNavigationGuard(corsGuard);
            navigationGuardMapping.setOrder(Integer.MIN_VALUE);
            navigationGuardMapping.addUrlPattern(corsRegistryBean.getUrlPattern());
            this.guardMappings.add(navigationGuardMapping);
        }

        // 扫描路由守卫
        for (NavigationGuard navigationGuard : BeanFactoryUtil.getBeansByType(applicationContext, NavigationGuard.class)) {
            Class<?> targetClass = AopUtil.getTargetClass(navigationGuard);
            Guard guard = targetClass.getAnnotation(Guard.class);
            if (guard != null) {
                // 获得顺序
                int order = guard.order();
                // 获得匹配路径
                String[] urlPatterns = guard.value();
                // 获得排序路径
                String[] exclude = guard.exclude();

                // 创建映射
                NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
                navigationGuardMapping.setNavigationGuard(navigationGuard);
                navigationGuardMapping.setUrlPatterns(urlPatterns);
                navigationGuardMapping.setExcludeUrl(exclude);
                navigationGuardMapping.setOrder(order);
                this.guardMappings.add(navigationGuardMapping);
            }
        }


    }


}
