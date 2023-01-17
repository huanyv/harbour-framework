package top.huanyv.start.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.*;
import top.huanyv.start.server.WebServer;

import javax.servlet.*;
import java.util.EventListener;

/**
 * @author huanyv
 * @date 2023/1/16 14:52
 */
public class JettyServer implements WebServer {

    /**
     * 端口号
     */
    private int port;

    private String contextPath;

    private final ServletContextHandler contextHandler;

    private Server server;

    public JettyServer() {
        this.contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    @Override
    public void start() {
        server = new Server(port);
        contextHandler.setContextPath(contextPath);
        server.setHandler(contextHandler);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            this.stop();
        }
    }

    @Override
    public void stop() {
        try {
            this.server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        ContextHandler.Context servletContext = contextHandler.getServletContext();
        return servletContext.addServlet(servletName, servlet);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        ContextHandler.Context servletContext = contextHandler.getServletContext();
        return servletContext.addFilter(filterName, filter);
    }

    @Override
    public void addListener(EventListener eventListener) {
        contextHandler.addEventListener(eventListener);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
