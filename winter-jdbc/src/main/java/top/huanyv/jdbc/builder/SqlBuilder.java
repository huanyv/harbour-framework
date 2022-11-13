package top.huanyv.jdbc.builder;

import top.huanyv.jdbc.anno.TableName;
import top.huanyv.tools.utils.StringUtil;

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
    protected Class<T> tableClass;

    public String sql() {
        return String.join(" ", this.sqlList);
    }

    public SqlBuilder<T> append(String sql) {
        this.sqlList.add(sql);
        return this;
    }

    public String getTableName() {
        TableName tableName = tableClass.getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return StringUtil.firstLetterLower(tableClass.getSimpleName());
    }

    public List<String> getSqlList() {
        return sqlList;
    }

    public Class<T> getTableClass() {
        return tableClass;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }

    public void setTableClass(Class<T> tableClass) {
        this.tableClass = tableClass;
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
