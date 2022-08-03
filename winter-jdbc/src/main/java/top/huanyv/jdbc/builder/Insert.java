package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/3 8:41
 */
public class Insert extends QueryBuilder {
    public Insert(Class<?> clazz) {
        super(new SqlBuilder<>());
        this.sqlBuilder.setTable(clazz);
        append("insert into").append(getTableName());
    }

    public Insert columns(String... columns) {
        append("(");
        for (int i = 0; i < columns.length; i++) {
            append(columns[i]);
            if (i < columns.length - 1) {
                append(",");
            }
        }
        append(")");
        return this;
    }

    public Insert values(Object... args) {
        setArguments(args);
        append("values(");
        for (int i = 0; i < args.length; i++) {
            append("?");
            if (i < args.length - 1) {
                append(",");
            }
        }
        append(")");
        return this;
    }

}
