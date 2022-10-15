package top.huanyv.web.servlet;

import top.huanyv.ioc.utils.AopUtil;
import top.huanyv.utils.IoUtil;
import top.huanyv.utils.WebUtil;
import top.huanyv.web.anno.*;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;
import top.huanyv.web.core.RequestHandler;
import top.huanyv.web.core.RequestMapping;
import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.exception.ExceptionHandler;
import top.huanyv.web.guard.NavigationGuardChain;
import top.huanyv.web.guard.NavigationGuardMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2022/7/29 9:22
 */
//public class RouterServlet extends InitRouterServlet {
public class RouterServlet extends InitProxyRouterServlet {

    @Override
    void doRouting(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String uri = WebUtil.getRequestURI(req);

        HttpRequest httpRequest = new HttpRequest(req, resp);
        httpRequest.setViewResolver(this.viewResolver);
        HttpResponse httpResponse = new HttpResponse(req, resp);

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
        // 获取当前uri的对应请求处理器映射
        RequestMapping mapping = this.requestRegistry.getMapping(uri);
        // 获取当前请求方式的处理
        RequestHandler requestHandler = mapping.getRequestHandler(requestMethod);

        // 设置pathVar
        mapping.parsePathVars(uri);

        // 判断处理器是否存在
        if (requestHandler != null) {
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
                .sorted((o1, o2) -> {
                    // 排序，如果序号一样，按照类名顺序
                    if (o1.getOrder() == o2.getOrder()) {
                        return o1.getNavigationGuard().getClass().getName()
                                .compareTo(o2.getNavigationGuard().getClass().getName());
                    }
                    return o1.getOrder() - o2.getOrder();
                }).collect(Collectors.toList());
        navigationGuardChain.setNavigationGuards(navigationGuardMappings);
        return navigationGuardChain;
    }

}
