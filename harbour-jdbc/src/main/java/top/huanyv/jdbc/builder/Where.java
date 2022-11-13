package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/1 17:31
 */
public class Where<T> extends QueryBuilder<T> {

    public Where(SqlBuilder<T> sqlBuilder, ConditionBuilder conditionBuilder) {
        super(sqlBuilder);
        if (!conditionBuilder.isEmpty()) {
            append("where");
            append(conditionBuilder.toString());
            setArguments(conditionBuilder.getArgs());
        }
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
