package top.huanyv.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.FilterHandler;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.servlet.*;
import top.huanyv.utils.ResourceUtil;
import top.huanyv.utils.StringUtil;
import top.huanyv.utils.SystemConstants;
import top.huanyv.view.StaticResourceHandler;
import top.huanyv.view.TemplateEngineInstance;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Winter {

    /**
     * 单例
     */
    private static Winter winter = new Winter();

    /**
     * web容器， 单例
     */
    private final Tomcat tomcat = new Tomcat();
    /**
     * 上下文对象
     */
    private Context context;

    RequestHandlerRegistry registry = RequestHandlerRegistry.single();

    private Winter() { }

    public static Winter getInstance() {
        return winter;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 初始化
     * @param port 端口
     */
    public void init(int port) {
        init(port, "");
    }
    public void init(int port, String ctx) {
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setURIEncoding(StandardCharsets.UTF_8.name());
        this.tomcat.getService().addConnector(connector);

        setCtx(ctx);
    }

    public void setCtx(String ctx) {
        this.context = tomcat.addContext(ctx, null);
    }

    /**
     * GET请求
     * @param pattern 请求地址
     * @param servletHandler 请求处理器
     */
    public void get(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.GET);
    }

    /**
     * POST请求
     * @param pattern 请求地址
     * @param servletHandler 请求处理器
     */
    public void post(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.POST);
    }
    public void put(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.PUT);
    }
    public void delete(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.DELETE);
    }

    /**
     * 所有请求
     * @param pattern 请求地址
     * @param servletHandler 请求处理器
     */
    public void request(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.GET);
        request(pattern, servletHandler, RequestMethod.POST);
        request(pattern, servletHandler, RequestMethod.PUT);
        request(pattern, servletHandler, RequestMethod.DELETE);
    }

    /**
     * 请求 注册到 处理器容器中
     */
    public void request(String pattern, ServletHandler servletHandler, RequestMethod method) {
        registry.register(pattern, method, servletHandler);
    }

    /**
     * 添加filter过滤器
     * @param urlPattern
     * @param filterHandler
     */
    public void filter(String urlPattern, FilterHandler filterHandler) {
        GlobalFilter globalFilter = new GlobalFilter();
        globalFilter.setFilterHandler(filterHandler);

        String uuid = StringUtil.getUUID();

        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(globalFilter);
        filterDef.setFilterName(uuid);
        this.context.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(uuid);
        filterMap.addURLPattern(urlPattern);
        this.context.addFilterMap(filterMap);
    }


    /**
     * 服务启动
     */
    public void start() {

        // 请求注册到tomcat容器中
        DispatcherServlet servlet = new DispatcherServlet();
        Wrapper dispatcher = this.tomcat.addServlet(this.context, "dispatcher", servlet);
        dispatcher.addMapping("/");

        // =======服务启动前初始化=======
        Properties properties = ResourceUtil.getProperties(SystemConstants.SYSTEM_CONFIG_FILE);
        String servletEncoding = properties.getProperty(SystemConstants.CONFIG_KEY_SERVLET_ENCODING, StandardCharsets.UTF_8.name());
        this.context.setResponseCharacterEncoding(servletEncoding);
        this.context.setRequestCharacterEncoding(servletEncoding);

        String thymeleafSuffix = properties.getProperty(SystemConstants.CONFIG_KEY_THYMELEAF_SUFFIX, SystemConstants.DEFAULT_THYMELEAF_SUFFIX);
        String thymeleafPrefix = properties.getProperty(SystemConstants.CONFIG_KEY_THYMELEAF_PREFIX, SystemConstants.DEFAULT_THYMELEAF_PREFIX);
        String staticPrefix = properties.getProperty(SystemConstants.CONFIG_KEY_STATIC_PREFIX, SystemConstants.DEFAULT_STATIC_PREFIX);
        TemplateEngineInstance.single().init(thymeleafPrefix, thymeleafSuffix);
        StaticResourceHandler.single().init(staticPrefix);
        // ===========================

        String banner = "__        ___       _            \n" +
                "\\ \\      / (_)_ __ | |_ ___ _ __ \n" +
                " \\ \\ /\\ / /| | '_ \\| __/ _ \\ '__|\n" +
                "  \\ V  V / | | | | | ||  __/ |   \n" +
                "   \\_/\\_/  |_|_| |_|\\__\\___|_|   ";

        System.out.println(banner);

        try {
            this.tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        this.tomcat.getServer().await();
    }


}
