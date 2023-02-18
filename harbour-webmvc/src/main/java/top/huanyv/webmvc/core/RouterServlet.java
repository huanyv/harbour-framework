package top.huanyv.webmvc.core;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.AopUtil;
import top.huanyv.webmvc.annotation.ExceptionPoint;
import top.huanyv.webmvc.core.request.FunctionRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.core.request.RequestMapping;
import top.huanyv.webmvc.core.request.ResourceRequestHandler;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.exception.ExceptionHandler;
import top.huanyv.webmvc.guard.NavigationGuardChain;
import top.huanyv.webmvc.guard.NavigationGuardMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端控制器，将请求到路由
 *
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
    void doRouting(HttpRequest request, HttpResponse response) throws Exception {
        // 获取请求处理器
        RequestHandler handler = getHandler(request);

        // 获取路由守卫执行链
        NavigationGuardChain navigationGuardChain = getNavigationGuardChain(request.getUri(), handler);
        // 路由守卫前置操作
        boolean handleBefore = navigationGuardChain.handleBefore(request, response);
        if (!handleBefore) {
            return;
        }

        // 处理执行
        handler.handle(request, response);

        // 路由守卫 后置操作
        navigationGuardChain.handleAfter(request, response);
    }

    private RequestHandler getHandler(HttpRequest request) throws Exception {
        RequestMethod requestMethod = RequestMethod.valueOf(request.method().toUpperCase());
        // 非用户定义请求
        if (RequestMethod.OPTIONS.equals(requestMethod)) {
            return new FunctionRequestHandler((req, resp) -> doOptions(req.raw(), resp.raw()));
        } else if (RequestMethod.HEAD.equals(requestMethod)) {
            return new FunctionRequestHandler((req, resp) -> doHead(req.raw(), resp.raw()));
        } else if (RequestMethod.TRACE.equals(requestMethod)) {
            return new FunctionRequestHandler((req, resp) -> doTrace(req.raw(), resp.raw()));
        }
        // 用户请求处理
        String uri = request.getUri();
        if (this.requestRegistry.containsRequest(uri)) {
            // 获取请求映射
            RequestMapping requestMapping = this.requestRegistry.getRequestMapping(uri);
            // 获取请求处理器
            RequestHandler requestHandler = requestMapping.getRequestHandler(requestMethod);
            if (requestHandler == null) {
                // 对应的请求方式没有注册
                return new FunctionRequestHandler((req, resp) ->
                        resp.error(405, "Request method '" + requestMethod.toString() + "' not supported."));
            }
            // 路径变量
            request.setPathVariables(requestMapping.parsePathVars(uri));
            return requestHandler;
        }
        // 静态资源
        return new ResourceRequestHandler(this.resourceHandler);
    }

    @Override
    void doException(HttpRequest req, HttpResponse resp, Exception ex) {
        Method exceptionMethod = null;
        exceptionHandler = (ExceptionHandler) AopUtil.getTargetObject(exceptionHandler);
        point:for (Method method : exceptionHandler.getClass().getDeclaredMethods()) {
            ExceptionPoint exceptionPoint = method.getAnnotation(ExceptionPoint.class);
            if (exceptionPoint != null) {
                for (Class<? extends Throwable> cls : exceptionPoint.value()) {
                    if (cls.isInstance(ex)) {
                        exceptionMethod = method;
                        break point;
                    }
                }
            }
        }

        if (exceptionMethod == null) {
            // 没有找到对应的异常处理器，使用默认处理
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

    private NavigationGuardChain getNavigationGuardChain(String uri, RequestHandler handler) {
        NavigationGuardChain navigationGuardChain = new NavigationGuardChain(handler);

        // 过滤，与url pattern匹配，不可与exclude url匹配
        List<NavigationGuardMapping> navigationGuardMappings = this.guardMappings.stream()
                .filter(navigationGuardMapping -> navigationGuardMapping.hasUrlPatten(uri)
                        && !navigationGuardMapping.isExclude(uri))
                .sorted((o1, o2) -> o1.getOrder() - o2.getOrder()).collect(Collectors.toList());
        navigationGuardChain.setNavigationGuards(navigationGuardMappings);
        return navigationGuardChain;
    }

}
