package top.huanyv.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.servlet.BaseWinterServlet;
import top.huanyv.servlet.GenericWinterServlet;
import top.huanyv.servlet.GetWinterServlet;
import top.huanyv.servlet.PostWinterServlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Winter {

    /**
     * 单例
     */
    private static Winter winter = new Winter();

    private final Tomcat tomcat = new Tomcat();
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

        this.context = tomcat.addContext(ctx, null);
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
        request(pattern, servletHandler, null);
    }

    /**
     * 指定请求处理
     */
    public void request(String pattern, ServletHandler servletHandler, RequestMethod method) {
        BaseWinterServlet servlet = null;

        if (RequestMethod.GET.equals(method)) {
            servlet = new GetWinterServlet();
        } else if(RequestMethod.POST.equals(method)) {
            servlet = new PostWinterServlet();
        } else if(RequestMethod.PUT.equals(method)) {
            servlet = new PostWinterServlet();
        } else if(RequestMethod.DELETE.equals(method)) {
            servlet = new PostWinterServlet();
        } else {
            servlet = new GenericWinterServlet();
        }

        servlet.setServletHandler(servletHandler);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        this.tomcat.addServlet(this.context, uuid, servlet);
        this.context.addServletMappingDecoded(pattern, uuid);
    }


    /**
     * 服务启动
     */
    public void start() {
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
