package top.huanyv.boot.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.boot.config.BootGlobalConfig;
import top.huanyv.utils.PropertiesUtil;
import top.huanyv.web.core.RequestHandlerRegistry;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;
import top.huanyv.web.servlet.RouterServlet;
import top.huanyv.web.servlet.WebApplicationListener;
import top.huanyv.web.utils.SystemConstants;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static top.huanyv.boot.config.BootGlobalConfig.*;

public class BootWinter {

    /**
     * 单例
     */
    private BootWinter() { }
    private static class SingleHolder {
        private static final BootWinter INSTANCE = new BootWinter();
    }
    public static BootWinter use() {
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



    /**
     * 请求注册器
     */
    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

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
    public void start(Class<?> mainClass, String[] args) {

        Properties properties = PropertiesUtil.getProperties("application.properties");
        // =======服务启动前初始化=======
        String port = properties.getProperty(CONFIG_KEY_SERVER_PORT, DEFAULT_SERVER_PORT);
        String contextPath = properties.getProperty(CONFIG_KEY_SERVER_CONTEXT, DEFAULT_SERVLET_CONTEXT);
        String encoding = properties.getProperty(CONFIG_KEY_GLOBAL_ENCODING, DEFAULT_GLOBAL_ENCODING);
        init(Integer.parseInt(port), contextPath, encoding);
        // ===========================

        String scanPackages = mainClass.getPackage().getName() + ", top.huanyv.boot";
        this.context.addParameter("ScanPackages", scanPackages);
        this.context.addApplicationListener(WebApplicationListener.class.getName());

        // 请求注册到tomcat容器中
        RouterServlet routerServlet = new RouterServlet();
        Wrapper dispatcher = this.tomcat.addServlet(this.context, SystemConstants.DISPATCHER_SERVLET_NAME, routerServlet);
        dispatcher.addMapping(SystemConstants.DISPATCHER_SERVLET_URL_PATTERN);



        String banner = "";
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
