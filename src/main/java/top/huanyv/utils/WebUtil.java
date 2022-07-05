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
        String[] vars = url.split("/");
        return vars[vars.length - 1].contains(".");
    }

    public static void responseHandle(HttpServletRequest req, HttpServletResponse resp, String responseInfo) throws ServletException, IOException {
        if (responseInfo.startsWith(SystemConstants.RESPONSE_FORWARD_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_FORWARD_PREFIX.length());
            req.getRequestDispatcher(responseInfo).forward(req, resp);
        } else if (responseInfo.startsWith(SystemConstants.RESPONSE_REDIRECT_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_REDIRECT_PREFIX.length());
            resp.sendRedirect(req.getContextPath() + responseInfo);
        } else if (responseInfo.startsWith(SystemConstants.RESPONSE_JSON_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_REDIRECT_PREFIX.length());
            resp.setContentType("application/json");
            resp.getWriter().println(responseInfo);
        } else  if (responseInfo.startsWith(SystemConstants.RESPONSE_HTML_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_HTML_PREFIX.length());
            resp.setContentType("text/html");
            resp.getWriter().println(responseInfo);
        } else  if (responseInfo.startsWith(SystemConstants.RESPONSE_TEXT_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_TEXT_PREFIX.length());
            resp.setContentType("text/plain");
            resp.getWriter().println(responseInfo);
        } else if(responseInfo.startsWith(SystemConstants.RESPONSE_TH_PREFIX)) {
            TemplateEngineInstance templateEngineInstance = TemplateEngineInstance.single();
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_TH_PREFIX.length());
            templateEngineInstance.process(responseInfo, req, resp);
        }else {
            resp.setContentType("text/html");
            resp.getWriter().println(responseInfo);
        }
    }

}
