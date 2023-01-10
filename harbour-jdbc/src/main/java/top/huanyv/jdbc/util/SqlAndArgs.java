package top.huanyv.jdbc.util;

import java.util.Arrays;

/**
 * @author huanyv
 * @date 2023/1/10 9:56
 */
public class SqlAndArgs {

    private String sql;

    private Object[] args;

    public SqlAndArgs() {
    }

    public SqlAndArgs(String sql, Object[] args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "SqlAndArgs{" +
                "sql='" + sql + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
