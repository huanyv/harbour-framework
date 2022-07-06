package top.huanyv.core;

import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.util.ClassUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import top.huanyv.annotation.Configuration;
import top.huanyv.config.GlobalConfiguration;
import top.huanyv.config.WebConfiguration;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.FilterHandler;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.servlet.*;
import top.huanyv.utils.ResourceUtil;
import top.huanyv.utils.StringUtil;
import top.huanyv.utils.SystemConstants;
import top.huanyv.view.StaticResourceHandler;
import top.huanyv.view.TemplateEngineInstance;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

public class Winter {

    /**
     * 单例
     */
    private Winter() { }

    private static class SingleHolder {
        private static final Winter INSTANCE = new Winter();
    }

    public static Winter use() {
        return SingleHolder.INSTANCE;
    }

    /**
     * web容器， 单例
     */
    private final Tomcat tomcat = new Tomcat();
    /**
     * 上下文对象
     */
    private Context context;

    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();
    private FilterHandlerRegistry filterRegistry = FilterHandlerRegistry.single();

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
        init(port, ctx, StandardCharsets.UTF_8.name());
    }
    public void init(int port, String ctx, String uriEncoding) {
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setURIEncoding(uriEncoding);
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
     */
    public void post(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.POST);
    }

    /**
     * PUT请求
     */
    public void put(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.PUT);
    }

    /**
     * delete请求
     */
    public void delete(String pattern, ServletHandler servletHandler) {
        request(pattern, servletHandler, RequestMethod.DELETE);
    }

    /**
     * 所有请求
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
        this.requestRegistry.register(pattern, method, servletHandler);
    }

    /**
     * 添加filter过滤器
     * @param filterHandler filter 处理器
     */
    public void filter(String urlPattern, FilterHandler filterHandler) {
        this.filterRegistry.register(urlPattern, filterHandler);
    }

    /**
     * 服务启动
     */
    public void start() {


        this.filterRegistry.toContext(this.context);

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
