package top.huanyv.start.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;

/**
 * @author huanyv
 * @date 2022/12/17 14:36
 */
public class TomcatServer implements WebServer{

    public TomcatServer(String contextPath) {
        setContextPath(contextPath);
    }

    /**
     * Tomcat容器
     */
    private final Tomcat tomcat = new Tomcat();

    /**
     * 上下文对象
     */
    private Context context;

    /**
     * 最大上传文件
     */
    private long maxFileSize;

    /**
     * 最大请求文件
     */
    private long maxRequestSize;

    /**
     * 端口号
     */
    private int port;

    private String contextPath;

    private String uriEncoding;

    public Wrapper addServlet(String servletName, Servlet servlet) {
        Wrapper wrapper = Tomcat.addServlet(context, servletName, servlet);
        wrapper.setMultipartConfigElement(new MultipartConfigElement("", maxFileSize, maxRequestSize, 0));
        return wrapper;
    }

    @Override
    public void start() {
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setURIEncoding(uriEncoding);
        this.tomcat.getService().addConnector(connector);
        try {
            this.tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        try {
            this.tomcat.stop();
            this.tomcat.destroy();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
        this.context = this.tomcat.addContext(contextPath, System.getProperty("java.io.tmpdir"));
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public void setUriEncoding(String uriEncoding) {
        this.uriEncoding = uriEncoding;
    }
}
