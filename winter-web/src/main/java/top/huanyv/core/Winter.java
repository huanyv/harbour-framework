package top.huanyv.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.config.WebConfiguration;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.FilterHandler;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.servlet.*;
import top.huanyv.utils.SystemConstants;
import top.huanyv.view.StaticResourceHandler;
import top.huanyv.view.TemplateEngineInstance;

import java.nio.charset.StandardCharsets;

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

    private WebConfiguration webConfig = new WebConfiguration() { };

    /**
     * web容器， 单例
     */
    private final Tomcat tomcat = new Tomcat();
    /**
     * 上下文对象
     */
    private Context context;


    private boolean initFlag = false;

    /**
     * 请求注册器
     */
    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();
    /**
     * 过滤器注册器
     */
    private FilterHandlerRegistry filterRegistry = FilterHandlerRegistry.single();

    public Context getContext() {
        return context;
    }

    /**
     * 设置配置类
     * @param webConfig 配置类
     */
    public void setConfig(WebConfiguration webConfig) {
        this.webConfig = webConfig;
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
        this.initFlag = true;
    }

    /**
     * 设置context-path
     * @param ctx context-path
     */
    public void setCtx(String ctx) {
        this.context = tomcat.addContext(ctx, System.getProperty("java.io.tmpdir"));
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
     * 添加一个视图跳转
     * @param urlPattern 地址
     * @param templateName 视图名称
     */
    public void addView(String urlPattern, String templateName) {
        request(urlPattern, (req, resp) -> req.view(templateName));
    }

    /**
     * 服务启动
     */
    public void start() {

        // =======服务启动前初始化=======
        String encoding = webConfig.getGlobalEncoding();
        String thPrefix = webConfig.getThymeleafPrefix();
        String thSuffix = webConfig.getThymeleafSuffix();
        String staticPrefix = webConfig.getStaticPrefix();
        int serverPort = webConfig.getServerPort();
        String contextPath = webConfig.getServerContext();


        if (!initFlag) {
            init(serverPort, contextPath, encoding);
        }

        this.context.setResponseCharacterEncoding(encoding);
        this.context.setRequestCharacterEncoding(encoding);

        TemplateEngineInstance.single().init(thPrefix, thSuffix);
        StaticResourceHandler.single().init(staticPrefix);
        // ===========================


        // 请求注册到tomcat容器中
        DispatcherServlet servlet = new DispatcherServlet();
        Wrapper dispatcher = this.tomcat.addServlet(this.context, SystemConstants.DISPATCHER_SERVLET_NAME, servlet);
        dispatcher.addMapping(SystemConstants.DISPATCHER_SERVLET_URL_PATTERN);

        // filter过滤器添加
        this.filterRegistry.toContext(this.context);

        String banner = webConfig.getBanner();
        System.out.println();
        System.out.println(banner);
        System.out.println();

        try {
            this.tomcat.start();
            this.tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
            try {
                this.tomcat.stop();
            } catch (LifecycleException lifecycleException) {
                lifecycleException.printStackTrace();
            }
        }
    }


}
