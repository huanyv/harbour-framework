package top.huanyv.start.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.start.utils.CommandLineUtil;
import top.huanyv.tools.utils.PropertiesUtil;
import top.huanyv.tools.utils.ResourceUtil;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;
import top.huanyv.webmvc.core.Routing;
import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandlerRegistry;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.interfaces.ServletHandler;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static top.huanyv.start.config.BootGlobalConfig.*;

public class Winter implements Routing, WebServer {

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



    /**
     * 请求注册器
     */
    private RequestHandlerRegistry requestRegistry = RequestHandlerRegistry.single();

    /**
     * 初始化
     * @param port 端口
     */
    private void init(int port) {
        init(port, "");
    }
    private void init(int port, String ctx) {
        init(port, ctx, StandardCharsets.UTF_8.name());
    }
    private void init(int port, String ctx, String uriEncoding) {
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
    private void setCtx(String ctx) {
        this.context = tomcat.addContext(ctx, System.getProperty("java.io.tmpdir"));
    }


    @Override
    public void register(String urlPattern, RequestMethod method, ServletHandler handler) {
        this.requestRegistry.registerHandler(urlPattern, method, new FunctionRequestHandler(handler));
    }

    /**
     * 服务启动
     */
    @Override
    public void start(Class<?> mainClass, String[] args) {
        CommandLineUtil commandLineUtil = new CommandLineUtil(args);
        // 获取命令行的环境参数名
        String envSuffix = commandLineUtil.getArgumentValue(ENV_COMMAND_LINE_ARGUMENT_NAME, null);
        // 如果为空，用默认的，否则用命令行
        String appEnv = DEFAULT_CONFIG_PROPERTIES;
        if (envSuffix != null) {
            appEnv = "application-" + envSuffix + ".properties";
        }

        Properties properties = PropertiesUtil.getProperties(appEnv);
        // =======服务启动前初始化=======
        // 获取设置端口，命令行 > 配置文件，默认8090
        String port = commandLineUtil.getArgumentValue(PORT_COMMAND_LINE_ARGUMENT_NAME
                , properties.getProperty(CONFIG_KEY_SERVER_PORT, DEFAULT_SERVER_PORT));
        String contextPath = properties.getProperty(CONFIG_KEY_SERVER_CONTEXT, DEFAULT_SERVLET_CONTEXT);
        String encoding = properties.getProperty(CONFIG_KEY_GLOBAL_ENCODING, DEFAULT_GLOBAL_ENCODING);
        init(Integer.parseInt(port), contextPath, encoding);
        // ===========================

        // 注册
        String scanPackages = mainClass.getPackage().getName() + ", top.huanyv.start";
//        this.context.addParameter(WebMvcGlobalConfig.WEB_BEAN_SCAN_PACKAGES, scanPackages);
//        this.context.addApplicationListener(WebApplicationListener.class.getName());

        // 文件配置
        String maxFileSize = properties.getProperty(CONFIG_FILE_MAX_FILE_SIZE, "1048576");
        String maxRequestSize = properties.getProperty(CONFIG_FILE_MAX_REQUEST_SIZE, "10485760");

        // 请求注册到tomcat容器中
        RouterServlet routerServlet = new RouterServlet();
        Wrapper router = this.tomcat.addServlet(this.context, WebMvcGlobalConfig.ROUTER_SERVLET_NAME, routerServlet);
        router.setMultipartConfigElement(new MultipartConfigElement("", Long.parseLong(maxFileSize), Long.parseLong(maxRequestSize), 0));
        router.addMapping("/");
        // 扫描包
        router.addInitParameter(WebMvcGlobalConfig.WEB_BEAN_SCAN_PACKAGES, scanPackages);
        router.setLoadOnStartup(1);

        // 获取banner
        String banner = ResourceUtil.readStr(BANNER_FILE_NAME, DEFAULT_BANNER);
        System.out.println();
        System.out.println(banner);
        System.out.println();

        try {
            this.tomcat.start();
            this.tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        try {
            this.tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }


}
