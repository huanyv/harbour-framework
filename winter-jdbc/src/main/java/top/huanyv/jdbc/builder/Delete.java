package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/2 17:56
 */
public class Delete extends QueryBuilder{
    public Delete() {
        super(new SqlBuilder());
        append("delete");
    }
    public <T> From<T> from(Class<T> table) {
        this.sqlBuilder.setTableClass(table);
        return new From<T>(this.sqlBuilder);
    }
}
