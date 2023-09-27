package top.huanyv.webmvc.core.request;

import top.huanyv.tools.utils.AntPathMatcher;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.enums.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 注册器
 * 注册请求，单例
 */
public class RequestHandlerRegistry {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

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
    private final List<RequestMapping> mappings = new ArrayList<>();


    public void register(String urlPattern, ActionResult result) {
        registerHandler(urlPattern, new FunctionRequestHandler(result));
    }

    public void registerHandler(String urlPattern, RequestHandler requestHandler) {
        registerHandler(urlPattern, RequestMethod.GET, requestHandler);
        registerHandler(urlPattern, RequestMethod.POST, requestHandler);
        registerHandler(urlPattern, RequestMethod.PUT, requestHandler);
        registerHandler(urlPattern, RequestMethod.DELETE, requestHandler);
    }

    public void registerHandler(String urlPattern, RequestMethod requestMethod, RequestHandler requestHandler) {
        Assert.notNull(urlPattern, "'urlPattern' must not be null.");
        Assert.isTrue(StringUtil.hasText(urlPattern), "'urlPattern' must not be empty.");
        Assert.notNull(requestMethod, "'requestMethod' must not be null.");
        Assert.notNull(requestHandler, "'requestHandler' must not be null.");
        // 精确匹配
        for (RequestMapping mapping : this.mappings) {
            if (urlPattern.equals(mapping.getUrlPattern())) {
                // 匹配到了
                mapping.addHandlerMapping(requestMethod, requestHandler);
                return;
            }
        }
        // 匹配不到，创建一个
        RequestMapping mapping = new RequestMapping();
        mapping.setUrlPattern(urlPattern);
        mapping.addHandlerMapping(requestMethod, requestHandler);
        this.mappings.add(mapping);
    }

    /**
     * 根据地址获取请求映射
     *
     * @param urlPattern 请求地址，一般是精确的
     * @return RequestMapping
     */
    public RequestMapping getRequestMapping(String urlPattern) {
        // 优先精确匹配URL地址
        for (RequestMapping mapping : this.mappings) {
            if (urlPattern.equals(mapping.getUrlPattern())) {
                // 精确匹配到了
                return mapping;
            }
        }
        for (RequestMapping mapping : this.mappings) {
            // if (mapping.compareUrl(urlPattern)) {
            //     return mapping;
            // }
            if (antPathMatcher.match(mapping.getUrlPattern(), urlPattern)) {
                return mapping;
            }
        }
        return null;
    }

    public List<RequestMapping> getMappings() {
        return Collections.unmodifiableList(this.mappings);
    }

}
