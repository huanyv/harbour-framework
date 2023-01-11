package top.huanyv.jdbc.core;

/**
 * @author huanyv
 * @date 2022/9/1 15:19
 */
public class SqlContextFactory {

    private static final ThreadLocal<SqlContext> sqlContextThreadLocal = new ThreadLocal<>();

    public static SqlContext getSqlContext() {
        SqlContext sqlContext = sqlContextThreadLocal.get();
        if (sqlContext == null) {
            sqlContext = new DefaultSqlContext();
            sqlContextThreadLocal.set(sqlContext);
        }
        return sqlContext;
    }

}
