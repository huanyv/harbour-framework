package top.huanyv.start.core;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.config.CliArguments;
import top.huanyv.start.server.NativeServletRegistry;
import top.huanyv.start.server.WebServer;
import top.huanyv.start.server.servlet.Registration;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.ResourceUtil;

import java.util.List;
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
        Assert.notNull(mainClass, "'mainClass' must not be null.");
        this.mainClass = mainClass;
    }

    public static ApplicationContext run(Class<?> mainClass, String... args) {
        return new HarbourApplication(mainClass).run(args);
    }

    public ApplicationContext run(String... args) {
        if (args == null) {
            args = new String[0];
        }
        CliArguments cliArguments = new CliArguments(args);
        this.appArguments = new AppArguments(cliArguments);

        // IOC
        ApplicationContext applicationContext = createApplicationContext();

        // 打印banner
        System.out.println(createBanner());

        if (isWebApplication()) {
            // 启动服务
            WebServer webServer = applicationContext.getBean(WebServer.class);
            // 注册原生的 Servlet
            registerServlet(applicationContext, webServer);
            webServer.start();
        }

        // 启动任务和定时任务
        callRunners(applicationContext);
        return applicationContext;
    }

    /**
     * 创建应用程序上下文
     *
     * @return {@link ApplicationContext}
     */
    public ApplicationContext createApplicationContext() {
        // 创建IOC容器
        ConditionConfigApplicationContext context = new ConditionConfigApplicationContext(mainClass.getPackage().getName());
        // 调用加载器
        context.callLoaders(appArguments);
        // 刷新容器
        context.refresh();
        return context;
    }

    public void callRunners(ApplicationContext applicationContext) {
        // 启动任务
        List<ApplicationRunner> runners = BeanFactoryUtil.getBeansByType(applicationContext, ApplicationRunner.class);
        // 排序
        runners.sort((o1, o2) -> o1.getOrder() - o2.getOrder());
        for (ApplicationRunner runner : runners) {
            runner.run(this.appArguments);
        }
        // 定时任务
        List<Timer> timers = BeanFactoryUtil.getBeansByType(applicationContext, Timer.class);
        if (!timers.isEmpty()) {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(timers.size());
            for (Timer timer : timers) {
                scheduledExecutorService.scheduleAtFixedRate(() -> timer.run(),
                        timer.getInitialDelay(), timer.getPeriod(), timer.getTimeUnit());
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

    public void registerServlet(ApplicationContext context, NativeServletRegistry servletRegistry) {
        List<Registration> registrations = BeanFactoryUtil.getBeansByType(context, Registration.class);
        for (Registration registration : registrations) {
            registration.addRegistration(servletRegistry);
        }
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
