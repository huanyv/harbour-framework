package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/2 8:33
 */
public class GroupBy<T> extends QueryBuilder<T>{

    public GroupBy(SqlBuild<T> sqlBuilder, String column) {
        super(sqlBuilder);
        append("group by").append(column);
    }


    public Having having() {
        return new Having(this.sqlBuilder);
    }

    public OrderBy orderBy() {
        return new OrderBy(this.sqlBuilder);
    }

    public Limit limit(int startIndex, int length) {
        return new Limit(this.sqlBuilder, startIndex, length);
    }
    public Limit limit(int length) {
        return new Limit(this.sqlBuilder, length);
    }
}
