package com.book.config;


import com.mysql.jdbc.Driver;
import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.jdbc.core.MapperScanner;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.jdbc.extend.SqlSessionFactoryBean;
import top.huanyv.web.config.ResourceMappingRegistry;
import top.huanyv.web.config.ViewControllerRegistry;
import top.huanyv.web.config.WebConfigurer;

import javax.sql.DataSource;

@Component
@Configuration
public class WebConfig implements WebConfigurer {

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.add("/", "index");
    }

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
    }

    @Bean
    public DataSource dataSource() {
        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        simpleDataSource.setDriverClassName(Driver.class.getName());
        simpleDataSource.setUsername("root");
        simpleDataSource.setPassword("2233");
        return simpleDataSource;
    }

    @Bean
    public MapperScanner mapperScanner() {
        return new MapperScanner("com.book");
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        return new SqlSessionFactoryBean();
    }
}
