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
    public From from(Class<?> table) {
        this.sqlBuilder.setTable(table);
        return new From(this.sqlBuilder);
    }
}
