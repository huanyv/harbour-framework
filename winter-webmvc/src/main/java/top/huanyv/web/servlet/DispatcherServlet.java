package top.huanyv.web.servlet;

import top.huanyv.web.anno.RequestPath;
import top.huanyv.web.config.ViewControllerRegistry;
import top.huanyv.web.config.WebConfigurer;
import top.huanyv.web.config.WebGlobalConfig;
import top.huanyv.web.core.*;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.utils.WebUtil;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;
import top.huanyv.web.view.StaticResourceHandler;
import top.huanyv.web.view.ViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 请求分发器
 * 所有请求都会到这个servlet
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 请求注册容器
     */
    private RequestHandlerRegistry requestRegistry;

    private ViewResolver viewResolver;

    @Override
    public void init() throws ServletException {
        requestRegistry = RequestHandlerRegistry.single();

        ServletContext servletContext = getServletContext();
        ApplicationContext applicationContext
                = (ApplicationContext)servletContext.getAttribute(WebGlobalConfig.WEB_APPLICATION_CONTEXT_ATTR_NAME);

        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            RequestPath requestPath = bean.getClass().getAnnotation(RequestPath.class);
            if (requestPath != null) {
                String path = requestPath.value();

                for (Method method : bean.getClass().getDeclaredMethods()) {
                    RequestPath methodRequestPath = method.getAnnotation(RequestPath.class);
                    if (methodRequestPath != null) {
                        path += methodRequestPath.value();
                        requestRegistry.register(path, methodRequestPath.method(), bean, method);
                    }
                }
            }
        }
        // get config class
        WebConfigurer webConfigurer = applicationContext.getBean(WebConfigurer.class);

        // template viewresolver
        this.viewResolver = applicationContext.getBean(ViewResolver.class);

        if (this.viewResolver != null) {
            // viewcontroller
            ViewControllerRegistry viewControllerRegistry = new ViewControllerRegistry();
            webConfigurer.addViewController(viewControllerRegistry);
            for (Map.Entry<String, String> entry : viewControllerRegistry.getViewController().entrySet()) {
                this.requestRegistry.register(entry.getKey(), (req, resp) -> req.view(entry.getValue()));
            }
        }


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = WebUtil.getRequestURI(req);

        // 处理静态资源
        boolean staticHandle = staticHandle(uri, resp);
        if (staticHandle) {
            return;
        }

        // 处理请求
//        int status = requestRegistry.handle(req, resp);
//        if (status == 404){
//            resp.sendError(404,"resources not found.");
//        } else if (status == 405) {
//            resp.sendError(405, "request method not exists.");
//        }

        if (!this.requestRegistry.containsRequest(uri)) {
            resp.sendError(404,"resources not found.");
            return;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod().toUpperCase());
        // 获取当前uri的对应请求处理器映射
        RequestMapping mapping = this.requestRegistry.getMapping(uri);
        // 获取当前请求方式的处理
        RequestHandler requestHandler = mapping.getRequestHandler(requestMethod);

        // 设置pathVar
        mapping.parsePathVars(uri);

        // 判断处理器是否存在
        if (requestHandler != null) {
            // 处理请求
            HttpRequest httpRequest = new HttpRequest(req, resp);
            httpRequest.setViewResolver(this.viewResolver);
            requestHandler.handle(httpRequest, new HttpResponse(req, resp));
        } else {
            // 这个请求方式没有注册
            resp.sendError(405, "request method not exists.");
        }


    }

    /**
     * 静态资源处理
     * @param uri 请求uri，为static下的文件路径
     * @param resp 响应对象
     * @return 是否存在静态资源
     */
    public boolean staticHandle(String uri, HttpServletResponse resp) throws IOException {
        StaticResourceHandler resourceHandler = StaticResourceHandler.single();
        // 判断是否有这个静态资源
        if (resourceHandler.hasResource(uri)) {
            // 跳转
            resourceHandler.process(uri, resp);
            return true;
        }
        return false;
    }

}
