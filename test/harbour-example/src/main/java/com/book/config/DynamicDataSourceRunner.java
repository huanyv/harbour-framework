package com.book.config;

import top.huanyv.bean.annotation.Component;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.DynamicDatasource;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;
import top.huanyv.tools.utils.ClassLoaderUtil;
import top.huanyv.tools.utils.PropertiesUtil;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2022/12/26 15:28
 */
//@Component
public class DynamicDataSourceRunner implements ApplicationRunner {

    @Override
    public void run(AppArguments appArguments) {
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

        SimpleDataSource ds1 = new SimpleDataSource();
        ds1.setDriverClassName("com.mysql.jdbc.Driver");
        ds1.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        ds1.setUsername("root");
        ds1.setPassword("2233");

        SimpleDataSource ds2 = new SimpleDataSource();
        ds2.setDriverClassName("com.mysql.jdbc.Driver");
        ds2.setUrl("jdbc:mysql://localhost:3306/temp?useSSL=false");
        ds2.setUsername("root");
        ds2.setPassword("2233");

        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        dynamicDatasource.setDefaultDataSource(ds1);
        dynamicDatasource.setDataSource("ds2", ds2);

        jdbcConfigurer.setDataSource(dynamicDatasource);

    }

    public static void main(String[] args) {
        String prefix = "jdbc.";
        InputStream inputStream = ClassLoaderUtil.getInputStream("application.properties");
        Map<String, String> properties = PropertiesUtil.loadMap(inputStream);
        Set<String> names = properties.entrySet().stream()
                .map(Map.Entry::getKey)
                .filter(key -> key.startsWith(prefix) && key.endsWith("url") && !(prefix + "url").equals(key))
                .map(key -> key.substring(prefix.length()).split("\\.")[0])
                .collect(Collectors.toSet());

        System.out.println("names = " + names);
        fun("s", null);
    }

    public static void fun(String s, Object... args) {
        System.out.println("args = " + args);
        System.out.println("args.length = " + args.length);
    }
}
