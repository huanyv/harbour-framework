package top.huanyv.start.core;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.bean.utils.OrderUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CommandLineArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.start.server.NativeServletRegistry;
import top.huanyv.start.server.WebServer;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.tools.utils.ResourceUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static top.huanyv.start.config.StartConstants.BANNER_FILE_NAME;
import static top.huanyv.start.config.StartConstants.FUOZU_BANNER;

/**
 * @author admin
 * @date 2022/7/6 16:46
 */
public class HarbourApplication {

    /**
     * 主方法类
     */
    private Class<?> mainClass;

    /**
     * 应用配置参数
     */
    private AppArguments appArguments;


    public HarbourApplication(Class<?> mainClass) {
        this.mainClass = mainClass;
    }

    public static ApplicationContext run(Class<?> mainClass, String... args) {
        return new HarbourApplication(mainClass).run(args);
    }

    public ApplicationContext run(String... args) {
        CommandLineArguments commandLineArguments = new CommandLineArguments(args);
        this.appArguments = new AppArguments(commandLineArguments);

        // IOC
        ApplicationContext applicationContext = createApplicationContext();

        // 打印banner
        System.out.println(createBanner());

        if (isWebApplication()) {
            // 启动服务
            WebServer webServer = applicationContext.getBean(WebServer.class);
            // 注册原生的 Servlet
            NativeServletRegistry.register(applicationContext, webServer);
            webServer.start();
        }

        // 启动任务和定时任务
        handleApplicationTask(applicationContext);
        return applicationContext;
    }

    /**
     * 创建应用程序上下文
     *
     * @return {@link ApplicationContext}
     */
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
        loaders.sort(new OrderUtil.OrderAsc());

        // 执行
        for (ApplicationLoader applicationLoader : loaders) {
            Class<? extends ApplicationLoader> cls = applicationLoader.getClass();

            // 获取配置属性前缀
            String propertiesPrefix = getPropertiesPrefix(cls);
            // 配置属性填充
            appArguments.populate(propertiesPrefix, applicationLoader);

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

    public void handleApplicationTask(ApplicationContext applicationContext) {
        // 启动任务
        List<ApplicationRunner> runners = BeanFactoryUtil.getBeansByType(applicationContext, ApplicationRunner.class);
        // 排序
        runners.sort(new OrderUtil.OrderAsc());
        for (ApplicationRunner runner : runners) {
            runner.run(this.appArguments);
        }
        // 定时任务
        List<SchedulingTask> tasks = BeanFactoryUtil.getBeansByType(applicationContext, SchedulingTask.class);
        if (!tasks.isEmpty()) {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(tasks.size());
            for (SchedulingTask task : tasks) {
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    task.run();
                }, task.getInitialDelay(), task.getPeriod(), task.getTimeUnit());
            }
        }
    }

    /**
     * 是不是一个Web应用程序
     *
     * @return boolean
     */
    public boolean isWebApplication() {
        return ClassUtil.isPresent("top.huanyv.webmvc.core.RouterServlet");
    }

    public String getPropertiesPrefix(Class<? extends ApplicationLoader> cls) {
        ConfigurationProperties annotation = cls.getAnnotation(ConfigurationProperties.class);
        if (annotation != null) {
            return annotation.prefix() + ".";
        }
        return "";
    }

    /**
     * 判断一个方法Bean是否注入到IOC中
     *
     * @param method             方法
     * @param applicationContext 应用程序上下文
     * @return boolean
     */
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

    /**
     * 创建启动图
     *
     * @return {@link String}
     */
    public String createBanner() {
        // 获取banner
        String banner = ResourceUtil.readStr(BANNER_FILE_NAME, FUOZU_BANNER);
        return "\n" + banner + "\n";
    }

    public void setAppArguments(AppArguments appArguments) {
        this.appArguments = appArguments;
    }
}
