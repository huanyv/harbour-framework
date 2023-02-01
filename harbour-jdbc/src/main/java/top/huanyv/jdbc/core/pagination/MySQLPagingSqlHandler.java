package top.huanyv.jdbc.core.pagination;

/**
 * @author huanyv
 * @date 2023/2/1 17:09
 */
public class MySQLPagingSqlHandler implements PagingSqlHandler {
    @Override
    public String build(String sql, int start, int offset) {
        if (start == 0) {
            return sql + " LIMIT " + offset;
        }
        return sql + " limit " + start + ", " + offset;
    }
}
