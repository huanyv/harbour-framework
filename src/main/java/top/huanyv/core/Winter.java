package top.huanyv.core;

import cn.hutool.core.util.ClassUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.servlet.*;
import top.huanyv.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    /**
     * 请求处理封装对象
     */
    private Map<String, Map<RequestMethod, ServletHandler>> requestHandlers = new HashMap<>();

    /**
     * web的根目录
     */
    private File webappPath;

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
        this.webappPath = new File(ClassUtil.getClassPath() + "templates");
        if (this.webappPath.exists()) {
            this.context = tomcat.addWebapp(ctx, this.webappPath.getAbsolutePath());
        } else {
            this.context = tomcat.addContext(ctx, null);
        }
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
        request(pattern, servletHandler, null);
    }

    /**
     * 请求注册到请求处理器中
     */
    public void request(String pattern, ServletHandler servletHandler, RequestMethod method) {
        Map<RequestMethod, ServletHandler> handler = null;
        if (this.requestHandlers.containsKey(pattern)) {
            handler = requestHandlers.get(pattern);
        } else {
            handler = new HashMap<>();
        }

        if (RequestMethod.GET.equals(method)) {
            handler.put(RequestMethod.GET, servletHandler);
        } else if(RequestMethod.POST.equals(method)) {
            handler.put(RequestMethod.POST, servletHandler);
        } else if(RequestMethod.PUT.equals(method)) {
            handler.put(RequestMethod.PUT, servletHandler);
        } else if(RequestMethod.DELETE.equals(method)) {
            handler.put(RequestMethod.DELETE, servletHandler);
        } else {
            handler.put(RequestMethod.GET, servletHandler);
            handler.put(RequestMethod.POST, servletHandler);
            handler.put(RequestMethod.PUT, servletHandler);
            handler.put(RequestMethod.DELETE, servletHandler);
        }
        this.requestHandlers.put(pattern, handler);
    }


    /**
     * 服务启动
     */
    public void start() {

        // 请求注册到tomcat容器中
        for (Map.Entry<String, Map<RequestMethod, ServletHandler>> entry : this.requestHandlers.entrySet()) {
            String pattern = entry.getKey();
            Map<RequestMethod, ServletHandler> handlerMap = entry.getValue();
            WinterServlet servlet = new WinterServlet();
            servlet.setRequestHandler(handlerMap);

            String uuid = StringUtil.getUUID();
            Wrapper wrapper = this.tomcat.addServlet(context, uuid, servlet);
            wrapper.addMapping(pattern);
        }

        // =======服务启动前初始化=======
        this.context.setResponseCharacterEncoding(StandardCharsets.UTF_8.name());
        this.context.setRequestCharacterEncoding(StandardCharsets.UTF_8.name());
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
