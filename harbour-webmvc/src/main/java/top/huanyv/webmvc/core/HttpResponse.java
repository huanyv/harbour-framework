package top.huanyv.webmvc.core;


import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.tools.utils.JsonUtil;
import top.huanyv.tools.utils.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;

    public HttpResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    /**
     * 得到原生的HttpServletRequest
     *
     * @return {@link HttpServletResponse}
     */
    public HttpServletResponse raw() {
        return servletResponse;
    }

    /**
     * 请求重定向，当location以 / 开头时会自动加上contextPath
     *
     * @param location 位置
     * @throws IOException ioexception
     */
    public void redirect(String location) throws IOException {
        if (location != null && location.startsWith("/")) {
            servletResponse.sendRedirect(servletRequest.getContextPath() + location);
            return;
        }
        servletResponse.sendRedirect(location);
    }

    public void html(String content) throws IOException {
        write(content, "text/html");
    }

    public void text(String content) throws IOException {
        write(content, "text/plain");
    }

    public void json(Object content) throws IOException {
        if (content instanceof CharSequence) {
            write((CharSequence) content, "application/json");
            return;
        }
        String json = JsonUtil.toJson(content);
        write(json, "application/json");
    }

    public void xml(String content) throws IOException {
        write(content, "text/xml");
    }

    public void write(CharSequence content, String contentType) throws IOException {
        servletResponse.setContentType(contentType);
        servletResponse.getWriter().print(content);
    }

    public void file(File file) throws IOException {
        file("", file);
    }

    public void file(String fileName, File file) throws IOException {
        Assert.notNull(file, "'file' must not be null.");
        if (!StringUtil.hasText(fileName)) {
            fileName = file.getName();
        }
        // 设置请求头
        servletResponse.setHeader("Content-Disposition", "attachment; filename="
                + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
        servletResponse.setHeader("Content-Length", String.valueOf(file.length()));
        servletResponse.setContentType("application/octet-stream");
        // 获取输入输出流
        ServletOutputStream outputStream = servletResponse.getOutputStream();
        FileInputStream inputStream = new FileInputStream(file);
        // 数据拷贝
        IoUtil.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    public void addCookie(String name, String value) {
        addCookie(name, value, -1);
    }

    public void addCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        servletResponse.addCookie(cookie);
    }

    // 原生
    public void addCookie(Cookie cookie) {
        servletResponse.addCookie(cookie);
    }

    public void error(int sc, String msg) throws IOException {
        servletResponse.sendError(sc, msg);
    }

    public void error(int sc) throws IOException {
        servletResponse.sendError(sc);
    }

    public void setHeader(String name, String value) {
        servletResponse.setHeader(name, value);
    }

    public void addHeader(String name, String value) {
        servletResponse.addHeader(name, value);
    }

    public void status(int sc) {
        servletResponse.setStatus(sc);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return servletResponse.getOutputStream();
    }

    public void contentType(String type) {
        servletResponse.setContentType(type);
    }

}
