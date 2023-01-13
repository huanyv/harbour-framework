package top.huanyv.webmvc.core;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.AopUtil;
import top.huanyv.bean.utils.OrderUtil;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.tools.utils.WebUtil;
import top.huanyv.webmvc.annotation.ExceptionPoint;
import top.huanyv.webmvc.core.request.MethodRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.core.request.RequestMapping;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.exception.ExceptionHandler;
import top.huanyv.webmvc.guard.NavigationGuardChain;
import top.huanyv.webmvc.guard.NavigationGuardMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端控制器，将请求到路由
 * @author admin
 * @date 2022/7/29 9:22
 */
public class RouterServlet extends InitRouterServlet {

    public RouterServlet() {
    }

    public RouterServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    void doRouting(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        HttpServletRequest req = httpRequest.raw();
        HttpServletResponse resp = httpResponse.raw();

        String uri = httpRequest.getUri();

        // 获取路由守卫执行链
        NavigationGuardChain navigationGuardChain = getNavigationGuardChain(req);
        // 路由守卫前置操作
        boolean handleBefore = navigationGuardChain.handleBefore(httpRequest, httpResponse);
        if (!handleBefore) {
            return;
        }

        // 处理请求
        if (!this.requestRegistry.containsRequest(uri)) {
            // 处理静态资源
            InputStream inputStream = resourceHandler.getInputStream(req);
            if (inputStream != null) {
                String mimeType = req.getServletContext().getMimeType(uri);
                resp.setContentType(mimeType);
                ServletOutputStream outputStream = resp.getOutputStream();
                IoUtil.copy(inputStream, outputStream);
            } else {
                // 静态资源不存在
                resp.sendError(404,"resources not found.");
            }
            return;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod().toUpperCase());
        if (RequestMethod.OPTIONS.equals(requestMethod)) {
            doOptions(req, resp);
            return;
        } else if (RequestMethod.HEAD.equals(requestMethod)) {
            doHead(req, resp);
            return;
        } else if (RequestMethod.TRACE.equals(requestMethod)) {
            doTrace(req, resp);
            return;
        }
        // 获取当前uri的对应请求处理器映射
        RequestMapping requestMapping = this.requestRegistry.getRequestMapping(uri);
        // 获取当前请求方式的处理
        RequestHandler requestHandler = requestMapping.getRequestHandler(requestMethod);

        // 设置pathVar
        httpRequest.setPathVariables(requestMapping.parsePathVars(uri));

        // 判断处理器是否存在
        if (requestHandler != null) {
            // 如果是Controller从容器中获取，多例
            if (requestHandler instanceof MethodRequestHandler) {
                MethodRequestHandler methodRequestHandler = (MethodRequestHandler) requestHandler;
                Object bean = applicationContext.getBean(methodRequestHandler.getController());
                methodRequestHandler.setControllerInstance(bean);
            }

            // 处理请求
            requestHandler.handle(httpRequest, httpResponse);
        } else {
            // 这个请求方式没有注册
            resp.sendError(405, "request method not exists.");
        }

        // 路由守卫 后置操作
        navigationGuardChain.handleAfter(httpRequest, httpResponse);
    }

    @Override
    void doException(HttpRequest req, HttpResponse resp, Exception ex) {
        Method exceptionMethod = null;
        exceptionHandler = (ExceptionHandler) AopUtil.getTargetObject(exceptionHandler);
        point:for (Method method : exceptionHandler.getClass().getDeclaredMethods()) {
            ExceptionPoint exceptionPoint = method.getAnnotation(ExceptionPoint.class);
            if (exceptionPoint != null) {
                for (Class<? extends Throwable> clazz : exceptionPoint.value()) {
                    if (clazz.isInstance(ex)) {
                        exceptionMethod = method;
                        break point;
                    }
                }
            }
        }

        if (exceptionMethod == null) {
            this.exceptionHandler.handle(req, resp, ex);
        } else {
            try {
                // 执行异常处理方法
                exceptionMethod.invoke(this.exceptionHandler, req, resp, ex);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }
    }

    public NavigationGuardChain getNavigationGuardChain(HttpServletRequest request) {
        // 请求uri
        String uri = WebUtil.getRequestURI(request);
        NavigationGuardChain navigationGuardChain = new NavigationGuardChain();

        // 过滤，与url pattern匹配，不可与exclude url匹配
        List<NavigationGuardMapping> navigationGuardMappings = this.guardMappings.stream()
                .filter(navigationGuardMapping -> navigationGuardMapping.hasUrlPatten(uri)
                        && !navigationGuardMapping.isExclude(uri))
                .sorted((o1, o2) -> o1.getOrder() - o2.getOrder()).collect(Collectors.toList());
        navigationGuardChain.setNavigationGuards(navigationGuardMappings);
        return navigationGuardChain;
    }

}
