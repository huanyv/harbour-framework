package top.huanyv.jdbc.builder;

import java.util.function.Consumer;

/**
 * @author admin
 * @date 2022/8/2 8:33
 */
public class GroupBy<T> extends QueryBuilder<T> {

    public GroupBy(SqlBuilder<T> sqlBuilder, String column) {
        super(sqlBuilder);
        append("group by").append(column);
    }

    public Having<T> having(Consumer<ConditionBuilder> consumer) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        consumer.accept(conditionBuilder);

        return new Having<T>(this.sqlBuilder, conditionBuilder);
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
