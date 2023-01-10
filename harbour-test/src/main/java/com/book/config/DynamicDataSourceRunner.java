package com.book.config;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Order;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.DynamicDatasource;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;

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
}
