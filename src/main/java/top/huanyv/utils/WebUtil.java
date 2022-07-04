package top.huanyv.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {

    public static String getRequestURI(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    public static String getExtension(String url) {
        return url.substring(url.lastIndexOf("."));
    }
}
