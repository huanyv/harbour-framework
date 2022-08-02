package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/2 8:33
 */
public class OrderBy<T> extends QueryBuilder<T> {

    public OrderBy(SqlBuild<T> sqlBuilder) {
        super(sqlBuilder);
        append("order by");
    }

    public OrderBy<T> asc(String column) {
        String endKeyWord = endKeyWord();
        if ("asc".equalsIgnoreCase(endKeyWord) || "desc".equalsIgnoreCase(endKeyWord)) {
            append(",");
        }
        append(column).append("asc");
        return this;
    }

    public OrderBy<T> desc(String column) {
        String endKeyWord = endKeyWord();
        if ("asc".equalsIgnoreCase(endKeyWord) || "desc".equalsIgnoreCase(endKeyWord)) {
           append(",");
        }
        append(column).append("desc");
        return this;
    }


    public Limit<T> limit(int startIndex, int length) {
        return new Limit<T>(this.sqlBuilder, startIndex, length);
    }
    public Limit<T> limit(int length) {
        return new Limit<T>(this.sqlBuilder, length);
    }


}
