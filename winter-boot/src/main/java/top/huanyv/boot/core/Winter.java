package top.huanyv.boot.core;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import top.huanyv.boot.config.BootGlobalConfig;
import top.huanyv.boot.utils.CommandLineUtil;
import top.huanyv.utils.PropertiesUtil;
import top.huanyv.utils.ResourceUtil;
import top.huanyv.web.config.WebMvcGlobalConfig;
import top.huanyv.web.core.RequestHandlerRegistry;
import top.huanyv.web.core.Routing;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;
import top.huanyv.web.servlet.RouterServlet;
import top.huanyv.web.servlet.WebApplicationListener;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static top.huanyv.boot.config.BootGlobalConfig.*;

public class Winter implements Routing {

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
        this.requestRegistry.register(urlPattern, method, handler);
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
        String scanPackages = mainClass.getPackage().getName() + ", top.huanyv.boot";
        this.context.addParameter(WebMvcGlobalConfig.WEB_BEAN_SCAN_PACKAGES, scanPackages);
        this.context.addApplicationListener(WebApplicationListener.class.getName());

        // 请求注册到tomcat容器中
        RouterServlet routerServlet = new RouterServlet();
        Wrapper router = this.tomcat.addServlet(this.context, WebMvcGlobalConfig.ROUTER_SERVLET_NAME, routerServlet);
        router.addMapping("/");
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
            try {
                this.tomcat.stop();
            } catch (LifecycleException lifecycleException) {
                lifecycleException.printStackTrace();
            }
        }
    }


}
