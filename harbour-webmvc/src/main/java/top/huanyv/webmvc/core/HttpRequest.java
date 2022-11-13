package top.huanyv.webmvc.core;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.tools.utils.WebUtil;
import top.huanyv.webmvc.enums.RequestMethod;
import top.huanyv.webmvc.view.ViewResolver;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.*;

public class HttpRequest {

    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;

    private ViewResolver viewResolver;

    private final String uri;
    private final RequestHandlerRegistry registry;


    public HttpRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.uri = WebUtil.getRequestURI(servletRequest);
        this.registry = RequestHandlerRegistry.single();
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    /**
     * 获取原生的request对象
     */
    public HttpServletRequest getOriginal() {
        return servletRequest;
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
        return IoUtil.readStr(servletRequest.getInputStream(), Charset.forName(servletRequest.getCharacterEncoding()));
    }

    public String pathVar(String name) {
        return registry.getMapping(uri).getPathVar(name);
    }

    /**
     * 多文件上传
     */
    public List<FileItem> getFileItems() {
        // 判断是否是文件
        if(ServletFileUpload.isMultipartContent(servletRequest)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());//工厂
            try {
                return servletFileUpload.parseRequest(servletRequest);//获取多段数据集合
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public FileItem getFileItem() {
        List<FileItem> fileItems = getFileItems();
        for (FileItem fileItem : fileItems) {
            if (!fileItem.isFormField()) {
                return fileItem;
            }
        }
        return null;
    }

    public String getParam(String name) {
        if (ServletFileUpload.isMultipartContent(servletRequest)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());//工厂
            try {
                List<FileItem> fileItems = servletFileUpload.parseRequest(servletRequest);//获取多段数据集合
                for (FileItem fileItem:fileItems) {
                    if(fileItem.isFormField()) { //不是文件
                        if (fileItem.getFieldName().equals(name)) {
                            return fileItem.getString(servletRequest.getCharacterEncoding());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getParameter(name);
    }


    // 原生方法
    public String getAuthType() {
        return servletRequest.getAuthType();
    }

    public Cookie[] getCookies() {
        return servletRequest.getCookies();
    }


    public long getDateHeader(String name) {
        return servletRequest.getDateHeader(name);
    }


    public String getHeader(String name) {
        return servletRequest.getHeader(name);
    }


    public Enumeration<String> getHeaders(String name) {
        return servletRequest.getHeaders(name);
    }


    public Enumeration<String> getHeaderNames() {
        return servletRequest.getHeaderNames();
    }


    public int getIntHeader(String name) {
        return servletRequest.getIntHeader(name);
    }


    public String getMethod() {
        return servletRequest.getMethod();
    }


    public String getPathInfo() {
        return servletRequest.getPathInfo();
    }


    public String getPathTranslated() {
        return servletRequest.getPathTranslated();
    }


    public String getContextPath() {
        return servletRequest.getContextPath();
    }


    public String getQueryString() {
        return servletRequest.getQueryString();
    }


    public String getRemoteUser() {
        return servletRequest.getRemoteUser();
    }


    public boolean isUserInRole(String role) {
        return servletRequest.isUserInRole(role);
    }


    public Principal getUserPrincipal() {
        return servletRequest.getUserPrincipal();
    }


    public String getRequestedSessionId() {
        return servletRequest.getRequestedSessionId();
    }


    public String getRequestURI() {
        return servletRequest.getRequestURI();
    }


    public StringBuffer getRequestURL() {
        return servletRequest.getRequestURL();
    }


    public String getServletPath() {
        return servletRequest.getServletPath();
    }


    public HttpSession getSession(boolean create) {
        return servletRequest.getSession(create);
    }


    public HttpSession getSession() {
        return servletRequest.getSession();
    }


    public String changeSessionId() {
        return servletRequest.changeSessionId();
    }


    public boolean isRequestedSessionIdValid() {
        return servletRequest.isRequestedSessionIdValid();
    }


    public boolean isRequestedSessionIdFromCookie() {
        return servletRequest.isRequestedSessionIdFromCookie();
    }


    public boolean isRequestedSessionIdFromURL() {
        return servletRequest.isRequestedSessionIdFromURL();
    }


    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return servletRequest.isRequestedSessionIdFromUrl();
    }


    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return servletRequest.authenticate(response);
    }


    public void login(String username, String password) throws ServletException {
        this.servletRequest.login(username, password);
    }


    public void logout() throws ServletException {
        this.servletRequest.logout();
    }


    public Collection<Part> getParts() throws IOException, ServletException {
        return servletRequest.getParts();
    }


    public Part getPart(String name) throws IOException, ServletException {
        return servletRequest.getPart(name);
    }


    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return servletRequest.upgrade(handlerClass);
    }


    public Object getAttribute(String name) {
        return servletRequest.getAttribute(name);
    }


    public Enumeration<String> getAttributeNames() {
        return servletRequest.getAttributeNames();
    }


    public String getCharacterEncoding() {
        return servletRequest.getCharacterEncoding();
    }


    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        this.servletRequest.setCharacterEncoding(env);
    }


    public int getContentLength() {
        return servletRequest.getContentLength();
    }


    public long getContentLengthLong() {
        return servletRequest.getContentLengthLong();
    }


    public String getContentType() {
        return servletRequest.getContentType();
    }


    public ServletInputStream getInputStream() throws IOException {
        return servletRequest.getInputStream();
    }

    public String getParameter(String name) {
        return servletRequest.getParameter(name);
    }

    public Enumeration<String> getParameterNames() {
        return servletRequest.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return servletRequest.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap() {
        return servletRequest.getParameterMap();
    }

    public String getProtocol() {
        return servletRequest.getProtocol();
    }

    public String getScheme() {
        return servletRequest.getScheme();
    }

    public String getServerName() {
        return servletRequest.getServerName();
    }

    public int getServerPort() {
        return servletRequest.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return servletRequest.getReader();
    }

    public String getRemoteAddr() {
        return servletRequest.getRemoteAddr();
    }

    public String getRemoteHost() {
        return servletRequest.getRemoteHost();
    }

    public void setAttribute(String name, Object o) {
        this.servletRequest.setAttribute(name, o);
    }

    public void removeAttribute(String name) {
        this.servletRequest.removeAttribute(name);
    }

    public Locale getLocale() {
        return servletRequest.getLocale();
    }

    public Enumeration<Locale> getLocales() {
        return servletRequest.getLocales();
    }


    public boolean isSecure() {
        return servletRequest.isSecure();
    }


    public RequestDispatcher getRequestDispatcher(String path) {
        return servletRequest.getRequestDispatcher(path);
    }

    @Deprecated
    public String getRealPath(String path) {
        return servletRequest.getRealPath(path);
    }


    public int getRemotePort() {
        return servletRequest.getRemotePort();
    }


    public String getLocalName() {
        return servletRequest.getLocalName();
    }


    public String getLocalAddr() {
        return servletRequest.getLocalAddr();
    }


    public int getLocalPort() {
        return servletRequest.getLocalPort();
    }


    public ServletContext getServletContext() {
        return servletRequest.getServletContext();
    }


    public AsyncContext startAsync() throws IllegalStateException {
        return servletRequest.startAsync();
    }


    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return servletRequest.startAsync(servletRequest, servletResponse);
    }


    public boolean isAsyncStarted() {
        return servletRequest.isAsyncStarted();
    }


    public boolean isAsyncSupported() {
        return servletRequest.isAsyncSupported();
    }


    public AsyncContext getAsyncContext() {
        return servletRequest.getAsyncContext();
    }


    public DispatcherType getDispatcherType() {
        return servletRequest.getDispatcherType();
    }
}
