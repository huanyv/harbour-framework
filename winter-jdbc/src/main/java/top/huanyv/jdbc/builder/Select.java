package top.huanyv.jdbc.builder;

/**
 * @author admin
 * @date 2022/8/1 17:00
 */
public class Select extends QueryBuilder {


    public Select(String s) {
        super(new SqlBuilder());
        append("select").append(s);
    }

    public Select() {
        super(new SqlBuilder());
        append("select *");
    }

    public <T> From<T> from(Class<T> table) {
        this.sqlBuilder.setTable(table);
        return new From<T>(this.sqlBuilder);
    }

}
