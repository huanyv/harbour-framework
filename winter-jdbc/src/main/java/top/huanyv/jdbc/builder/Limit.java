package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/2 8:51
 */
public class Limit<T> extends QueryBuilder<T> {
    public Limit(SqlBuild<T> sqlBuilder, int startIndex, int length) {
        super(sqlBuilder);
        append("limit").append(String.valueOf(startIndex))
                .append(",").append(String.valueOf(length));
    }
    public Limit(SqlBuild<T> sqlBuilder,  int length) {
        super(sqlBuilder);
        append("limit").append(String.valueOf(length));
    }
}
