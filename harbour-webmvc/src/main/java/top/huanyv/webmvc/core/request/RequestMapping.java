package top.huanyv.webmvc.core.request;

import top.huanyv.webmvc.config.WebMvcGlobalConfig;
import top.huanyv.webmvc.enums.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestMapping {

    public static final String REQUEST_URI_REGULAR = "\\{[0-9a-zA-Z]+\\}";

    /**
     * 模糊请求地址，可以是模糊地址，比如 /user/{id}/{name}
     * 对应的变量和值会在请求过来后，到pathVariables中
     */
    private String urlPattern;

    /**
     * 请求与处理器映射
     * 格式为：
     * {
     *     GET: handler1
     *     POST: handler2
     *     PUT: handler3
     *     DELETE: handler4
     * }
     */
    private Map<RequestMethod, RequestHandler> handlerMapping;

    /**
     * 通过请求方法，获取具体的处理器，如果不存在返回null
     * @param method 请求方式
     * @return 具体处理器
     */
    public RequestHandler getRequestHandler(RequestMethod method) {
        return handlerMapping.get(method);
    }

    /**
     * 模糊比较地址
     * @param url 精确地址
     * @return 是否相同
     */
    public boolean compareUrl(String url) {
        if (Objects.equals(this.urlPattern, url)) {
            return true;
        }
        // 模糊地址
        String[] arr1 = this.urlPattern.split(WebMvcGlobalConfig.PATH_SEPARATOR);
        // 精确地址
        String[] arr2 = url.split(WebMvcGlobalConfig.PATH_SEPARATOR);
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i].matches(REQUEST_URI_REGULAR)) {
                continue;
            }
            if (!arr1[i].equals(arr2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 添加处理程序映射
     *
     * @param method  方法
     * @param handler 处理程序
     * @return {@link RequestMapping}
     */
    public RequestMapping addHandlerMapping(RequestMethod method, RequestHandler handler) {
        this.handlerMapping.put(method, handler);
        return this;
    }

    /**
     * 从精确地址中解析出变量值来
     * @param url 精确地址
     */
    public Map<String, String> parsePathVars(String url) {
        Map<String, String> pathVariables = new HashMap<>();
        if (url == null) {
            return pathVariables;
        }
        // 模糊地址
        String[] arr1 = this.urlPattern.split(WebMvcGlobalConfig.PATH_SEPARATOR);
        // 精确地址
        String[] arr2 = url.split(WebMvcGlobalConfig.PATH_SEPARATOR);
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i].matches(REQUEST_URI_REGULAR)) {
                pathVariables.put(arr1[i].substring(1, arr1[i].length() - 1), arr2[i]);
            }
        }
        return pathVariables;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public void setHandler(Map<RequestMethod, RequestHandler> handler) {
        this.handlerMapping = handler;
    }

    @Override
    public String toString() {
        return "RequestMapping{" +
                "urlPattern='" + urlPattern + '\'' +
                ", handler=" + handlerMapping +
                '}';
    }
}
