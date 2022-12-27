package com.book.config;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.start.server.servlet.FilterBean;
import top.huanyv.start.server.servlet.ServletBean;
import top.huanyv.start.server.servlet.ServletListenerBean;
import top.huanyv.start.web.WebConfiguration;
import top.huanyv.tools.web.filter.CorsFilter;
import top.huanyv.webmvc.config.CorsRegistry;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.ViewControllerRegistry;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Configuration
public class WebConfig extends WebConfiguration {

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        super.addViewController(registry);
        registry.add("/", "index");
        registry.add("/login", "login");
    }

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        super.addResourceMapping(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
//        registry.addMapping("/**")
//            // 设置允许跨域请求的域名
//            .allowedOriginPatterns("*")
//            // 是否允许cookie
//            .allowCredentials(true)
//            // 设置允许的请求方式
//            .allowedMethods("GET", "POST", "DELETE", "PUT")
//            // 设置允许的header属性
//            .allowedHeaders("*")
//            // 跨域允许时间
//            .maxAge(3600L);
        registry.addMapping("/**").defaultRule();
    }

    @Bean
    public ServletBean testServletBean() {
        return new ServletBean(new TestServlet(), "/test");
    }

    @Bean
    public FilterBean testFilterBean() {
        return new FilterBean(new TestFilter(), "/*");
    }

    @Bean
    public ServletListenerBean servletListenerBean() {
        return new ServletListenerBean(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                System.out.println("ServletContext监听器");
            }
        });
    }

    public static class TestServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.getWriter().print("Servlet!");
        }
    }

    public static class TestFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("过滤器");
            chain.doFilter(request, response);
        }
    }

}
