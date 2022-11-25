package com.book.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.support.DaoScanner;
import top.huanyv.webmvc.config.CorsRegistry;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.ViewControllerRegistry;
import top.huanyv.webmvc.config.WebConfigurer;

@Component
@Configuration
public class WebConfig implements WebConfigurer {

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.add("/", "index");
        registry.add("/login", "login");
    }

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
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
    public DaoScanner daoScanner() {
        // 加载配置
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        druidDataSource.setDriverClassName(Driver.class.getName());
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("2233");

        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        simpleDataSource.setDriverClassName(Driver.class.getName());
        simpleDataSource.setUsername("root");
        simpleDataSource.setPassword("2233");

        jdbcConfigurer.setDataSource(druidDataSource);
        jdbcConfigurer.setScanPackages("com.book");

        return new DaoScanner();
    }
}