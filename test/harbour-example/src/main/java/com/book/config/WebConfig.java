package com.book.config;

import com.wf.captcha.servlet.CaptchaServlet;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.start.server.servlet.ServletBean;
import top.huanyv.start.web.WebConfiguration;
import top.huanyv.webmvc.config.CorsRegistry;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.ViewControllerRegistry;


@Bean
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
    public ServletBean captchaServlet() {
        return new ServletBean(new CaptchaServlet(), "/captcha");
    }

}
