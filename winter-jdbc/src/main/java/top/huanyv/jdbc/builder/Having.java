package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/1 17:31
 */
public class Having<T> extends QueryBuilder<T>{

    public Having(SqlBuilder<T> sqlBuilder) {
        super(sqlBuilder);
        append("having");
    }

    public Having<T> append(String sql, Object... args) {
        append(sql);
        setArguments(args);
        return this;
    }

    public Having<T> and(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("having")) {
            append("and");
        }
        setArguments(args);
        append(sql);
        return this;
    }

    public Having<T> or(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("having")) {
            append("or");
        }
        setArguments(args);
        append(sql);
        return this;
    }

    public Having<T> append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }
    public Having<T> and(boolean condition, String sql, Object... args) {
        if (condition) {
            and(sql, args);
        }
        return this;
    }

    public Having or(boolean condition, String sql, Object... args) {
        if (condition) {
            or(sql, args);
        }
        return this;
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
