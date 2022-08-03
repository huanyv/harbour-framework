package com.book;

import com.mysql.jdbc.Driver;
import top.huanyv.boot.core.WinterApplication;
import top.huanyv.jdbc.core.ConnectionHolder;
import top.huanyv.jdbc.extend.SimpleDataSource;

import java.sql.SQLException;

public class BookmanagementApplication {

    public static void main(String[] args) {

        WinterApplication.run(BookmanagementApplication.class, args);
    }

}
