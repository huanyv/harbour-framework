package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/2 17:36
 */
public class Update<T> extends QueryBuilder<T> {
    public Update(Class<T> table) {
        super(new SqlBuild<>());
        this.sqlBuilder.setTable(table);
        append("update").append(getTableName()).append("set");
    }



    public Update<T> append(String sql, Object... args) {
        if (!endKeyWord().equalsIgnoreCase("set")) {
            append(",");
        }
        append(sql);
        setArguments(args);
        return this;
    }

    public Update<T> append(boolean condition, String sql, Object... args) {
        if (condition) {
            append(sql, args);
        }
        return this;
    }


    public Where<T> where() {
        return new Where<T>(this.sqlBuilder);
    }
}
