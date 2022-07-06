package top.huanyv.utils;

import top.huanyv.view.TemplateEngineInstance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {

    public static String getRequestURI(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    public static String getExtension(String url) {
        return url.substring(url.lastIndexOf("."));
    }

    public static boolean hasExtension(String url) {
        String[] vars = url.split(SystemConstants.PATH_SEPARATOR);
        return vars[vars.length - 1].contains(".");
    }


}
