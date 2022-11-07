package top.huanyv.web.core;

import top.huanyv.web.enums.RequestMethod;
import top.huanyv.web.interfaces.ServletHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 注册器
 * 注册请求，单例
 */
public class RequestHandlerRegistry {

    // 单例
    private RequestHandlerRegistry() {
    }

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


    public void register(String urlPattern, ServletHandler servletHandler) {
        registerHandler(urlPattern, new FunctionRequestHandler(servletHandler));
    }

    public void registerHandler(String urlPattern, RequestHandler requestHandler) {
        registerHandler(urlPattern, RequestMethod.GET, requestHandler);
        registerHandler(urlPattern, RequestMethod.POST, requestHandler);
        registerHandler(urlPattern, RequestMethod.PUT, requestHandler);
        registerHandler(urlPattern, RequestMethod.DELETE, requestHandler);
    }

    public void registerHandler(String urlPattern, RequestMethod requestMethod, RequestHandler requestHandler) {
        RequestMapping mapping = getMapping(urlPattern);
        mapping.getHandler().put(requestMethod, requestHandler);
    }


    /**
     * 注册容器中是否有该地址
     *
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
//    public Map<RequestMethod, ServletHandler> getHandler(String urlPattern) {
//        return getMapping(urlPattern).getHandler();
//    }

    /**
     * 根据地址获取请求映射
     *
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


}
