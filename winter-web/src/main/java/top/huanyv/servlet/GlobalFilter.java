package top.huanyv.servlet;


import top.huanyv.core.HttpRequest;
import top.huanyv.core.HttpResponse;
import top.huanyv.interfaces.FilterHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalFilter implements Filter {

    private FilterHandler filterHandler;

    public void setFilterHandler(FilterHandler filterHandler) {
        this.filterHandler = filterHandler;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = null;
        HttpServletRequest req = null;
        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
        }
        if (response instanceof HttpServletResponse) {
            resp = (HttpServletResponse) response;
        }
        filterHandler.handle(new HttpRequest(req, resp), new HttpResponse(req, resp), chain);
    }

    @Override
    public void destroy() {

    }
}
