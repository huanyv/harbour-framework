package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.anno.TableName;
import top.huanyv.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/1 16:57
 */
public class SqlBuilder<T> {
    private List<String> sqlList = new ArrayList<>();
    private List<Object> arguments = new ArrayList<>();
    protected Class<T> table;

    public String sql() {
        return String.join(" ", this.sqlList);
    }

    public SqlBuilder<T> append(String sql) {
        this.sqlList.add(sql);
        return this;
    }

    public String getTableName() {
        TableName tableName = table.getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return StringUtil.firstLetterLower(table.getSimpleName());
    }

    public List<String> getSqlList() {
        return sqlList;
    }

    public Class<T> getTable() {
        return table;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }

    public void setTable(Class<T> table) {
        this.table = table;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public void setArguments(Object... args) {
        this.arguments.addAll(Arrays.asList(args));
    }


    @Override
    public String toString() {
        return "SqlBuild{" +
                "sql=" + sqlList +
                '}';
    }
}
