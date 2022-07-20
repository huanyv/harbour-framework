package top.huanyv.core;

import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.utils.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册器
 * 注册请求，单例
 */
public class RequestHandlerRegistry {

    // 单例
    private RequestHandlerRegistry() {}
    private static class SingletonHolder {
        private static final RequestHandlerRegistry INSTANCE = new RequestHandlerRegistry();
    }
    // 提供单例对象
    public static RequestHandlerRegistry single() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 注册容器，所有的请求都会注册到这个容器中
     */
    private List<RequestMapping> registry = new ArrayList<>();

    public List<RequestMapping> getRegistry() {
        return registry;
    }

    /**
     * 请求注册到注册容器中
     * @param urlPattern 请求地址
     * @param method 请求方式
     * @param servletHandler 请求处理器
     */
    public void register(String urlPattern, RequestMethod method, ServletHandler servletHandler) {
        RequestMapping mapping = getMapping(urlPattern);
        mapping.getHandler().put(method, servletHandler);
    }

    /**
     * 注册容器中是否有该地址
     * @param urlPattern 地址，一般是精确的
     * @return true/false
     */
    public boolean containsRequest(String urlPattern) {
        for (RequestMapping mapping : this.registry) {
            if (mapping.compareUrl(urlPattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据地址获取处理器映射
     * @param urlPattern 地址，一般是精确的
     */
    public Map<RequestMethod, ServletHandler> getHandler(String urlPattern) {
        return getMapping(urlPattern).getHandler();
    }

    /**
     * 根据地址获取请求映射
     * @param urlPattern 请求地址，一般是精确的
     * @return RequestMapping
     */
    public RequestMapping getMapping(String urlPattern) {
        for (RequestMapping mapping : this.registry) {
            if (mapping.compareUrl(urlPattern)) {
                return mapping;
            }
        }
        // 不存在，新建一个,添加进去
        RequestMapping mapping = new RequestMapping();
        mapping.setUrlPattern(urlPattern);
        mapping.setHandler(new HashMap<>());
        this.registry.add(mapping);
        return mapping;

    }

    /**
     * 处理请求
     * @param req 请求
     * @param resp 响应
     * @return 状态码，没有注册这个请求，404；注册了，但没有注册这个请求方式405；处理成功，200
     */
    public int handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uri = WebUtil.getRequestURI(req);

        // 这个请求没有注册
        if (!containsRequest(uri)) {
            return 404;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod().toUpperCase());
        // 获取当前uri的对应请求处理器映射
        RequestMapping mapping = getMapping(uri);
        // 获取当前请求方式的处理
        ServletHandler servletHandler = mapping.getRequestHandler(requestMethod);

        // 设置pathVar
        mapping.parsePathVars(uri);

        // 判断处理器是否存在
        if (servletHandler != null) {
            // 处理请求
            servletHandler.handle(new HttpRequest(req, resp), new HttpResponse(req, resp));
            return 200;
        } else {
            // 这个请求方式没有注册
            return 405;
        }
    }


}
