package top.huanyv.jdbc.core.pagination;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/2/1 17:23
 */
public class PagingSqlFactory {

    private final static Map<String, PagingSqlHandler> handlers = new HashMap<>();

    static {
        handlers.put("mysql", new MySQLPagingSqlHandler());
        handlers.put("sqlite", new MySQLPagingSqlHandler());
        handlers.put("oracle", new OraclePagingSqlHandler());
        handlers.put("sqlserver", new SqlServerPagingSqlHandler());
    }

    public static PagingSqlHandler getPageSql(String dbType) {
        return handlers.get(dbType);
    }

    /**
     * 设置分页处理程序
     *
     * @param dbType  数据库类型，小写字母
     * @param handler 处理程序
     */
    public static void setHandler(String dbType, PagingSqlHandler handler) {
        handlers.put(dbType, handler);
    }

}
