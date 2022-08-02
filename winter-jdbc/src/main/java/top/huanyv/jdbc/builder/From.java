package top.huanyv.jdbc.builder;

import top.huanyv.utils.StringUtil;

/**
 * @author admin
 * @date 2022/8/1 17:02
 */
public class From<T> extends QueryBuilder<T> {

    public From(SqlBuild<T> sqlBuilder) {
        super(sqlBuilder);
        append("from").append(getTableName());
    }

    public Where<T> where() {
        return new Where<T>(this.sqlBuilder);
    }

    public GroupBy<T> groupBy(String column) {
        return new GroupBy<T>(this.sqlBuilder, column);
    }

    public OrderBy<T> orderBy() {
        return new OrderBy<T>(this.sqlBuilder);
    }
    public Limit<T> limit(int startIndex, int length) {
        return new Limit<T>(this.sqlBuilder, startIndex, length);
    }
    public Limit<T> limit(int length) {
        return new Limit<T>(this.sqlBuilder, length);
    }
}
