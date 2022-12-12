package top.huanyv.webmvc.utils;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;

import javax.servlet.ServletContext;

/**
 * @author huanyv
 * @date 2022/11/7 16:13
 */
public class ServletHolder {

    private static ServletContext servletContext;

    private static ThreadLocal<HttpRequest> requestThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<HttpResponse> responseThreadLocal = new ThreadLocal<>();

    public static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }


    public static void setRequest(HttpRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static void removeRequest() {
        requestThreadLocal.remove();
    }


    public static void setResponse(HttpResponse response) {
        responseThreadLocal.set(response);
    }

    public static HttpResponse getResponse() {
        return responseThreadLocal.get();
    }

    public static void removeResponse() {
        responseThreadLocal.remove();
    }

}
