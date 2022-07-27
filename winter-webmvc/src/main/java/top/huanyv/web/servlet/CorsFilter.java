package top.huanyv.web.servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author admin
 * @date 2022/7/27 15:47
 */
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("filter");
    }

    @Override
    public void destroy() {

    }
}
