package top.huanyv.webmvc.utils;

import top.huanyv.bean.utils.IoUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class WebUtil {

    public static String getRequestURI(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (requestURI.equals(contextPath)) {
            return "/";
        }
        return requestURI.substring(contextPath.length());
    }

    /**
     * 响应Json数据
     */
    public static void responseJsonData(HttpServletResponse response, String data) {
        responseData(response,"application/json",data);
    }

    /**
     * 响应普通文本数据
     */
    public static void responseTextData(HttpServletResponse response, String data) {
        responseData(response,"text/plain",data);
    }

    /**
     * 响应HTML数据
     */
    public static void responseHtmlData(HttpServletResponse response, String data) {
        responseData(response,"text/html",data);
    }

    /**
     * 响应数据
     * @param response 响应对象
     * @param contentType 响应数据类型
     * @param data 响应数据
     */
    public static void responseData(HttpServletResponse response, String contentType, String data) {
        try {
            response.setStatus(200);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(contentType);
            response.getWriter().print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求体
     */
    public static String getRequestBody(HttpServletRequest req) throws IOException {
        return getRequestBody(req, StandardCharsets.UTF_8);
    }
    public static String getRequestBody(HttpServletRequest req, Charset charset) throws IOException {
        req.setCharacterEncoding(charset.name());
        ServletInputStream inputStream = req.getInputStream();
        return IoUtil.readStr(inputStream, charset);
    }

}
