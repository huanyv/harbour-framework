package top.huanyv.start.loader.tomcat;

import org.apache.catalina.Wrapper;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Order;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.start.server.WebServer;
import top.huanyv.start.tomcat.TomcatServer;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.Servlet;
import java.nio.charset.StandardCharsets;

/**
 * @author huanyv
 * @date 2022/12/17 17:09
 */
@ConfigurationProperties(prefix = "server")
public class TomcatStartLoader implements ApplicationLoader {

    /**
     * 最大上传文件
     */
    private long maxFileSize = 1048576;

    /**
     * 最大请求文件
     */
    private long maxRequestSize = 10485760;

    /**
     * 端口号
     */
    private int port = 2333;

    /**
     * 项目路径
     */
    private String contextPath = "";

    /**
     * 编码
     */
    private String uriEncoding = StandardCharsets.UTF_8.name();

    /**
     * 前端控制器
     */
    private Servlet servlet;

    @Override
    public void load(ApplicationContext applicationContext, AppArguments appArguments) {
        // 创建前端控制器
        this.servlet = new RouterServlet(applicationContext);
    }

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public WebServer webServer() {
        TomcatServer tomcatServer = new TomcatServer(this.contextPath);
        tomcatServer.setPort(this.port);
        tomcatServer.setMaxFileSize(this.maxFileSize);
        tomcatServer.setMaxRequestSize(this.maxRequestSize);
        tomcatServer.setUriEncoding(this.uriEncoding);

        // 请求注册到tomcat容器中
        Wrapper wrapper = tomcatServer.addServlet(WebMvcGlobalConfig.ROUTER_SERVLET_NAME, servlet);
        wrapper.setLoadOnStartup(1);
        wrapper.addMapping("/");

        return tomcatServer;
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, WebServer.class);
        }
    }
}
