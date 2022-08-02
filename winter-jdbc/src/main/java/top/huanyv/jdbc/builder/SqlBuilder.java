package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.core.Page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 16:16
 */
public class SqlBuilder {

    public static void main(String[] args) {
        String[] strings = {"1", "2", "a", "b"};
//        test(strings);
        test(1,2,3,4);
    }

    public static void test(Object... objects) {
        test1(objects);
    }
    public static void test1(Object... objects) {
        System.out.println(objects);
        System.out.println("objects = " + Arrays.toString(objects));
    }

    private StringBuilder sql;
    private List<Object> arguments;


    public SqlBuilder(String sql) {
        this.sql = new StringBuilder(sql);
        this.arguments = new ArrayList<>();
    }

    public SqlBuilder append(String sql, Object arg) {
        this.sql.append(" ").append(sql);
        this.arguments.add(arg);
        return this;
    }

    public SqlBuilder append(boolean condition ,String sql, Object arg) {
        if (condition) {
            this.sql.append(" ").append(sql);
            this.arguments.add(arg);
        }
        return this;
    }

    public <T> List<T> selectList() {

        return null;
    }

    public <T> T selectOne() {

        return null;
    }

    public <T> Page<T> page(int pageNum, int pageSize) {
        int limitBegin = (pageNum - 1) * pageSize;
        int limitLen = pageSize;



        return null;
    }

}
