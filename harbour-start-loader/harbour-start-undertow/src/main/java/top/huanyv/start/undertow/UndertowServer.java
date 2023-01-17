package top.huanyv.start.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.core.ManagedListener;
import io.undertow.servlet.handlers.ServletHandler;
import io.undertow.servlet.spec.FilterRegistrationImpl;
import io.undertow.servlet.spec.ServletRegistrationImpl;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import top.huanyv.start.server.WebServer;

import javax.servlet.*;
import java.util.EventListener;

/**
 * TODO 添加servlet失败问题，filter等也不行，但前端控制器正常使用
 *
 * @author huanyv
 * @date 2023/1/16 14:52
 */
public class UndertowServer implements WebServer {

    /**
     * 端口号
     */
    private int port;

    private String contextPath;

    private DeploymentManager deploymentManager;

    public UndertowServer(String contextPath) {
        // 部署信息
        DeploymentInfo deploymentInfo = Servlets.deployment();
        deploymentInfo.setClassLoader(Thread.currentThread().getContextClassLoader())
                .setContextPath(contextPath)
                .setDeploymentName("harbour-start");


        // 部署
        ServletContainer container = Servlets.defaultContainer();
        deploymentManager = container.addDeployment(deploymentInfo);
        deploymentManager.deploy();
    }

    @Override
    public void start() {

        HttpHandler start = null;
        try {
            start = deploymentManager.start();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        PathHandler pathHandler = Handlers.path();
        pathHandler.addPrefixPath("/", start);
        Undertow server = Undertow.builder()
                .addHttpListener(this.port, "")
                .setHandler(start).build();

        server.start();
    }

    @Override
    public void stop() {

    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        ServletInfo servletInfo = new ServletInfo(servletName, servlet.getClass(), new ImmediateInstanceFactory<>(servlet));
        Deployment deployment = this.deploymentManager.getDeployment();
        ServletHandler handler = deployment.getServlets().addServlet(servletInfo);
        return new ServletRegistrationImpl(servletInfo, handler.getManagedServlet(), deployment);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        FilterInfo filterInfo = new FilterInfo(filterName, filter.getClass(), new ImmediateInstanceFactory<>(filter));
        Deployment deployment = this.deploymentManager.getDeployment();
        return new FilterRegistrationImpl(filterInfo, deployment, deployment.getServletContext());
    }

    @Override
    public void addListener(EventListener eventListener) {
        Deployment deployment = this.deploymentManager.getDeployment();
        ListenerInfo listenerInfo = new ListenerInfo(eventListener.getClass(), new ImmediateInstanceFactory<>(eventListener));
        deployment.getApplicationListeners().addListener(new ManagedListener(listenerInfo, false));
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
