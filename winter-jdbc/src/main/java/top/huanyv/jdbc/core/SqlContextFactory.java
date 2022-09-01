package top.huanyv.jdbc.core;

import javax.sql.DataSource;

/**
 * @author huanyv
 * @date 2022/9/1 15:19
 */
public class SqlContextFactory {

    private static ThreadLocal<SqlContext> sqlContext = new ThreadLocal<>();

    public static SqlContext getSqlContext() {
        if (sqlContext.get() == null) {
            sqlContext.set(new SqlContext());
        }
        return sqlContext.get();
    }

}
