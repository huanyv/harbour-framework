package top.huanyv.jdbc.core.datasource;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataSourceKeyHolderTest {

    @Test
    public void get() {
        DataSourceKeyHolder.set("ds2");
        System.out.println("1 = " + DataSourceKeyHolder.get());
        DataSourceKeyHolder.remove();
        System.out.println("2 = " + DataSourceKeyHolder.get());
    }
}