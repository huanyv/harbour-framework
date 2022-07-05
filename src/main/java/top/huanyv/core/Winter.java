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
import top.huanyv.utils.StringUtil;
import top.huanyv.view.StaticResourceHandler;
import top.huanyv.view.TemplateEngineInstance;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

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

    private Winter() { }

    public static Winter getInstance() {
        return winter;
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
        RequestHandlerRegistry registry = RequestHandlerRegistry.single();
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
        this.context.setResponseCharacterEncoding(StandardCharsets.UTF_8.name());
        this.context.setRequestCharacterEncoding(StandardCharsets.UTF_8.name());

        TemplateEngineInstance.single().init("templates", ".html");
        StaticResourceHandler.single().init("static");
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
