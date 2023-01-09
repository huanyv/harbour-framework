package com.book;
import java.io.PrintWriter;
import top.huanyv.jdbc.core.datasource.ConnectionPool;

import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.DynamicDatasource;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.start.core.HarbourApplication;

public class MainApplication {

    public static void main(String[] args) {
        HarbourApplication.run(MainApplication.class, args);

/*        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

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

        jdbcConfigurer.setDataSource(dynamicDatasource);*/
    }

}
