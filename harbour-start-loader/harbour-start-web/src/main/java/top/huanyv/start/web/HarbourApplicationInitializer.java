package top.huanyv.start.web;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Order;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CommandLineArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author huanyv
 * @date 2022/12/24 16:37
 */
public abstract class HarbourApplicationInitializer implements WebStartupInitializer {

    private Class<?> mainClass;

    private AppArguments appArguments;

    @Override
    public void onStartup(ServletContext ctx) throws ServletException {
        this.mainClass = run();
        AppArguments appArguments = new AppArguments();
        appArguments.load(new CommandLineArguments());
        this.appArguments = appArguments;

        ApplicationContext applicationContext = createApplicationContext();

        RouterServlet routerServlet = new RouterServlet(applicationContext);
        ServletRegistration.Dynamic router = ctx.addServlet(WebMvcGlobalConfig.ROUTER_SERVLET_NAME, routerServlet);
        router.setLoadOnStartup(1);
        router.setMultipartConfig(new MultipartConfigElement("",
                appArguments.getLong("server.maxFileSize", "1048576"),
                appArguments.getLong("server.maxRequestSize", "10485760"), 0));
        router.addMapping("/");

    }

    public ApplicationContext createApplicationContext() {
        // 创建IOC容器
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(mainClass.getPackage().getName());
        // 执行启动加载器
        appStartLoad(applicationContext);
        // 刷新容器
        applicationContext.refresh();
        return applicationContext;
    }

    /**
     * 应用程序启动加载
     *
     * @param applicationContext 应用程序上下文
     */
    public void appStartLoad(ApplicationContext applicationContext) {
        List<ApplicationLoader> loaders = new ArrayList<>();
        // 获取SPI，应用启动加载器
        ServiceLoader<ApplicationLoader> applicationLoaders = ServiceLoader.load(ApplicationLoader.class);
        for (ApplicationLoader applicationLoader : applicationLoaders) {
            loaders.add(applicationLoader);
        }
        // 排序
        loaders.sort((o1, o2) -> {
            Order o1Order = o1.getClass().getAnnotation(Order.class);
            Order o2Order = o2.getClass().getAnnotation(Order.class);
            if (o1Order != null && o2Order != null) {
                return o1Order.value() - o2Order.value();
            }
            return 0;
        });
        // 执行
        for (ApplicationLoader applicationLoader : loaders) {
            Class<? extends ApplicationLoader> cls = applicationLoader.getClass();

            // 获取配置属性前缀
            String propertiesPrefix = getPropertiesPrefix(cls);
            // 配置属性填充
            appArguments.populate(propertiesPrefix + ".", applicationLoader);

            // 方法Bean注入
            for (Method method : cls.getDeclaredMethods()) {
                if (isMatcher(method, applicationContext)) {
                    BeanDefinition beanDefinition = new MethodBeanDefinition(applicationLoader, method);
                    applicationContext.registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
                }
            }
            // 执行加载方法
            applicationLoader.load(applicationContext, appArguments);
        }
    }

    public boolean isMatcher(Method method, ApplicationContext applicationContext) {
        // 如果没有@Bean注解，不注入
        if (!method.isAnnotationPresent(Bean.class)) {
            return false;
        }
        Conditional conditional = method.getAnnotation(Conditional.class);
        if (conditional != null) {
            // 有条件，条件通过注入
            Condition condition = ReflectUtil.newInstance(conditional.value());
            return condition.matchers(applicationContext, this.appArguments);
        }
        // 如果没有条件注解，直接注入
        return true;
    }

    public String getPropertiesPrefix(Class<? extends ApplicationLoader> cls) {
        ConfigurationProperties annotation = cls.getAnnotation(ConfigurationProperties.class);
        if (annotation != null) {
            return annotation.prefix();
        }
        return "";
    }

    public abstract Class<?> run();

}
