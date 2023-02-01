package top.huanyv.jdbc.core.pagination;

/**
 * @author huanyv
 * @date 2023/2/1 17:09
 */
public class OraclePagingSqlHandler implements PagingSqlHandler {

    @Override
    public String build(String sql, int start, int offset) {
        return "SELECT * FROM ( SELECT t1.*, ROWNUM ROW_ID FROM ( " +
                sql + " ) t1 WHERE ROWNUM <= " + (start + offset) + ") WHERE ROW_ID > " + start;
    }

}
