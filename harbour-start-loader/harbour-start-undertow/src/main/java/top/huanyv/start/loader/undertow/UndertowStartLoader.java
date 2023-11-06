package top.huanyv.start.loader.undertow;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.start.server.WebServer;
import top.huanyv.start.undertow.UndertowServer;
import top.huanyv.bean.utils.FileUtil;
import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import java.nio.charset.StandardCharsets;

/**
 * @author huanyv
 * @date 2022/12/17 17:09
 */
@Properties(prefix = "server.")
public class UndertowStartLoader implements ApplicationLoader {


    /**
     * 最大上传文件
     */
    private String maxFileSize = "1MB";

    /**
     * 最大请求文件
     */
    private String maxRequestSize = "10MB";


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
        UndertowServer server = new UndertowServer(this.contextPath);
        server.setPort(this.port);
        ServletRegistration.Dynamic servletRegistration = server.addServlet(WebMvcGlobalConfig.ROUTER_SERVLET_NAME, servlet);
        servletRegistration.addMapping("/");
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.setMultipartConfig(new MultipartConfigElement("",
                FileUtil.parseSize(maxFileSize), FileUtil.parseSize(maxRequestSize), 0));
        return server;
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, WebServer.class);
        }
    }
}
