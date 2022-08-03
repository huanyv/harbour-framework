package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/1 17:31
 */
public class Where<T> extends QueryBuilder<T> {

    public Where(SqlBuilder<T> sqlBuilder) {
        super(sqlBuilder);
        append("where");
    }

    @Override
    public Where<T> append(String sql) {
        super.append(sql);
        return this;
    }

    public Where<T> append(String sql, Object... args) {
        append(sql);
        setArguments(args);
        return this;
    }

    public Where<T> and(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("where")) {
            append("and");
        }
        setArguments(args);
        append(sql);
        return this;
    }

    public Where<T> or(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("where")) {
            append("or");
        }
        setArguments(args);
        append(sql);
        return this;
    }

    public Where<T> append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }
    public Where<T> and(boolean condition, String sql, Object... args) {
        if (condition) {
            and(sql, args);
        }
        return this;
    }

    public Where<T> or(boolean condition, String sql, Object... args) {
        if (condition) {
            or(sql, args);
        }
        return this;
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
