package top.huanyv.jdbc.core.pagination;

/**
 * @author huanyv
 * @date 2023/2/1 17:09
 */
public class SqlServerPagingSqlHandler implements PagingSqlHandler {

    @Override
    public String build(String sql, int start, int offset) {
        return sql + " OFFSET " + offset + " ROWS FETCH NEXT " + start + " ROWS ONLY";
    }

}
