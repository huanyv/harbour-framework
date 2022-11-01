package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/1 17:31
 */
public class Having<T> extends QueryBuilder<T>{

    public Having(SqlBuilder<T> sqlBuilder, ConditionBuilder conditionBuilder) {
        super(sqlBuilder);
        if (!conditionBuilder.isEmpty()) {
            append("having");
            append(conditionBuilder.toString());
            setArguments(conditionBuilder.getArgs());
        }
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
