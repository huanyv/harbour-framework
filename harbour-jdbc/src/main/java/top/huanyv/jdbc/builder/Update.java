package top.huanyv.jdbc.builder;

import java.util.function.Consumer;

/**
 * @author admin
 * @date 2022/8/2 17:36
 */
public class Update extends QueryBuilder {
    public Update(Class<?> table) {
        super(new SqlBuilder<>());
        this.sqlBuilder.setTableClass(table);
        append("update").append(getTableName()).append("set");
    }

    public Update append(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("set")) {
            append(",");
        }
        append(sql);
        setArguments(args);
        return this;
    }

    public Update append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }

    public Where where(Consumer<ConditionBuilder> consumer) {
        ConditionBuilder conditionBuilder = new ConditionBuilder();
        consumer.accept(conditionBuilder);
        return new Where<>(this.sqlBuilder, conditionBuilder);
    }
}
