package top.huanyv.core;

import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerRegistry {

    private RequestHandlerRegistry() {}

    private static class SingletonHolder {
        private static final RequestHandlerRegistry INSTANCE = new RequestHandlerRegistry();
    }

    // 提供单例对象
    public static RequestHandlerRegistry single() {
        return SingletonHolder.INSTANCE;
    }


    private Map<String, Map<RequestMethod, ServletHandler>> registry = new HashMap<>();

    public Map<String, Map<RequestMethod, ServletHandler>> getRegistry() {
        return registry;
    }

    /**
     * 注册一个处理器
     * @param pattern 请求地址
     * @param requestHandler 请求处理器
     */
    public void register(String pattern, Map<RequestMethod, ServletHandler> requestHandler) {
        this.registry.put(pattern, requestHandler);
    }

    public boolean containsRequest(String urlPattern) {
        return this.registry.containsKey(urlPattern);
    }

    public Map<RequestMethod, ServletHandler> getHandler(String urlPattern) {
        if (containsRequest(urlPattern)) {
            return this.registry.get(urlPattern);
        }
        return new HashMap<>();
    }

    public static Map<RequestMethod, ServletHandler> newHandler() {
        return new HashMap<>();
    }



}
