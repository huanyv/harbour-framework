package top.huanyv.jdbc.core.pagination;

/**
 * @author huanyv
 * @date 2023/2/1 17:06
 */
public interface PagingSqlHandler {

    default String handle(String sql, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        sql = sql.trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return build(sql, start, pageSize);
    }

    /**
     * 构建分页SQL
     *
     * @param sql    sql语句
     * @param start  起始
     * @param offset 偏移量
     * @return {@link String}
     */
    String build(String sql, int start, int offset);
}
