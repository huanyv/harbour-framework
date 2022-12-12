package top.huanyv.webmvc.core;

import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.tools.utils.WebUtil;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.view.ViewResolver;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class HttpRequest {

    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;

    private ViewResolver viewResolver;

    private final String uri;

    private byte[] requestBody;

    /**
     * 路径变量，key为变量名，value为值
     * key 从模糊请求地址上得到
     * value从请求过来后具体的请求地址上得到
     */
    private Map<String, String> pathVariables;

    public HttpRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.uri = WebUtil.getRequestURI(servletRequest);
        this.pathVariables = new HashMap<>();
    }

    /**
     * 获取原生的request对象
     */
    public HttpServletRequest raw() {
        return servletRequest;
    }

    public String getUri() {
        return this.uri;
    }

    /**
     * 请求转发
     * @param path 转发地址
     */
    public void forward(String path) throws ServletException, IOException {
        servletRequest.getRequestDispatcher(path).forward(servletRequest, servletResponse);
    }

    /**
     * 转发视图
     * @param name 视图名
     */
    public void view(String name) throws IOException, ServletException {
        Assert.notNull(this.viewResolver, () -> {
            try {
                servletResponse.sendError(500, "View resolver not config!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "View resolver not config!";
        });
        this.viewResolver.render(name, servletRequest, servletResponse);
    }

    /**
     * 获取请求体
     */
    public String body() throws IOException {
        String method = servletRequest.getMethod();
        if (RequestMethod.GET.name().equalsIgnoreCase(method)) {
            return servletRequest.getQueryString();
        }
        if (requestBody == null) {
            ServletInputStream inputStream = servletRequest.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IoUtil.copy(inputStream, outputStream);
            this.requestBody = outputStream.toByteArray();
            outputStream.close();
            inputStream.close();
        }
        return new String(this.requestBody, servletRequest.getCharacterEncoding());
    }

    public long paramLong(String name) {
        return Long.parseLong(param(name));
    }

    public int paramInt(String name) {
        return Integer.parseInt(param(name));
    }

    public String param(String name) {
        return servletRequest.getParameter(name);
    }

    public long pathLong(String name) {
        return Long.parseLong(pathVar(name));
    }

    public int pathInt(String name) {
        return Integer.parseInt(pathVar(name));
    }

    public String pathVar(String name) {
        return this.pathVariables.get(name);
    }

    public String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = servletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public Cookie[] getCookies() {
        return servletRequest.getCookies();
    }

    public String getHeader(String name) {
        return servletRequest.getHeader(name);
    }

    public String method() {
        return servletRequest.getMethod();
    }

    public String ctxPath() {
        return servletRequest.getContextPath();
    }

    public HttpSession getSession() {
        return servletRequest.getSession();
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return servletRequest.getParts();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return servletRequest.getPart(name);
    }

    public Object getAttr(String name) {
        return servletRequest.getAttribute(name);
    }

    public void setAttr(String name, Object o) {
        this.servletRequest.setAttribute(name, o);
    }

    public void removeAttr(String name) {
        this.servletRequest.removeAttribute(name);
    }

    public String getContentType() {
        return servletRequest.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return servletRequest.getInputStream();
    }

    public int port() {
        return servletRequest.getServerPort();
    }

    public ServletContext ctx() {
        return servletRequest.getServletContext();
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public ViewResolver getViewResolver() {
        return viewResolver;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }
}
