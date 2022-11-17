package top.huanyv.webmvc.core;


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
import java.util.Collection;
import java.util.Locale;

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
    public HttpServletResponse getOriginal() {
        return servletResponse;
    }

    /**
     * 请求重定向，当location以 / 开头时会自动加上contextPath
     *
     * @param location 位置
     * @throws IOException ioexception
     */
    public void redirect(String location) throws IOException {
        if (location.startsWith("/")) {
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

    public boolean containsHeader(String name) {
        return servletResponse.containsHeader(name);
    }

    public String encodeURL(String url) {
        return servletResponse.encodeURL(url);
    }

    public String encodeRedirectURL(String url) {
        return servletResponse.encodeRedirectURL(url);
    }

    @Deprecated
    public String encodeUrl(String url) {
        return servletResponse.encodeUrl(url);
    }

    @Deprecated
    public String encodeRedirectUrl(String url) {
        return servletResponse.encodeRedirectUrl(url);
    }

    public void sendError(int sc, String msg) throws IOException {
        servletResponse.sendError(sc, msg);
    }

    public void sendError(int sc) throws IOException {
        servletResponse.sendError(sc);
    }

    public void sendRedirect(String location) throws IOException {
        servletResponse.sendRedirect(location);
    }

    public void setDateHeader(String name, long date) {
        servletResponse.setDateHeader(name, date);
    }

    public void addDateHeader(String name, long date) {
        servletResponse.addDateHeader(name, date);
    }

    public void setHeader(String name, String value) {
        servletResponse.setHeader(name, value);
    }

    public void addHeader(String name, String value) {
        servletResponse.addHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        servletResponse.setIntHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        servletResponse.addIntHeader(name, value);
    }

    public void setStatus(int sc) {
        servletResponse.setStatus(sc);
    }

    @Deprecated
    public void setStatus(int sc, String sm) {
        servletResponse.setStatus(sc, sm);
    }

    public int getStatus() {
        return servletResponse.getStatus();
    }

    public String getHeader(String name) {
        return servletResponse.getHeader(name);
    }

    public Collection<String> getHeaders(String name) {
        return servletResponse.getHeaders(name);
    }

    public Collection<String> getHeaderNames() {
        return servletResponse.getHeaderNames();
    }

    public String getCharacterEncoding() {
        return servletResponse.getCharacterEncoding();
    }

    public String getContentType() {
        return servletResponse.getContentType();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return servletResponse.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        return servletResponse.getWriter();
    }

    public void setCharacterEncoding(String charset) {
        servletResponse.setCharacterEncoding(charset);
    }

    public void setContentLength(int len) {
        servletResponse.setContentLength(len);
    }

    public void setContentLengthLong(long len) {
        servletResponse.setContentLengthLong(len);
    }

    public void setContentType(String type) {
        servletResponse.setContentType(type);
    }

    public void setBufferSize(int size) {
        servletResponse.setBufferSize(size);
    }

    public int getBufferSize() {
        return servletResponse.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        servletResponse.flushBuffer();
    }

    public void resetBuffer() {
        servletResponse.resetBuffer();
    }

    public boolean isCommitted() {
        return servletResponse.isCommitted();
    }

    public void reset() {
        servletResponse.reset();
    }

    public void setLocale(Locale loc) {
        servletResponse.setLocale(loc);
    }

    public Locale getLocale() {
        return servletResponse.getLocale();
    }
}
