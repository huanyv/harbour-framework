package top.huanyv.webmvc.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanyv
 * @date 2022/11/7 16:13
 */
public class ServletHolder {

    private static ServletContext servletContext;

    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    public static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }


    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static void removeRequest() {
        requestThreadLocal.remove();
    }


    public static void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }

    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    public static void removeResponse() {
        responseThreadLocal.remove();
    }

}
