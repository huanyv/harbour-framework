package top.huanyv.jdbc.util;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 * @author huanyv
 * @date 2023/2/24 16:26
 */
public class SqlUtil {

    public static String generateInsert(Class<?> cls) {
        return generateInsert(cls, true);
    }

    public static String generateInsert(Class<?> cls, boolean c2u) {
        StringBuilder sql = new StringBuilder("insert into ").append(StringUtil.firstLetterLower(cls.getSimpleName()));
        // (column1, column2, column3, .....)
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        // (?, ?, ?, ......)
        StringJoiner values = new StringJoiner(", ", "(", ")");

        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // 驼峰转下划线
            String columnName = c2u ? StringUtil.camelCaseToUnderscore(fieldName) : fieldName;
            // 自定义列名
            Column column = field.getAnnotation(Column.class);
            columnName = column != null ? column.value() : columnName;
            columns.add(columnName);
            values.add("#{" + fieldName + "}");
        }
        sql.append(columns).append(" values").append(values);
        return sql.toString();
    }

    public static String generateUpdate(Class<?> cls) {
        return generateUpdate(cls, true);
    }

    public static String generateUpdate(Class<?> cls, boolean c2u) {
        StringBuilder sql = new StringBuilder("update ").append(StringUtil.firstLetterLower(cls.getSimpleName())).append(" set ");
        // column1 = ?, column2 = ?, ......
        StringJoiner columns = new StringJoiner(", ");
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // 驼峰转下划线
            String columnName = c2u ? StringUtil.camelCaseToUnderscore(fieldName) : fieldName;
            // 自定义列名
            Column column = field.getAnnotation(Column.class);
            columnName = column != null ? column.value() : columnName;
            columns.add(columnName + " = #{" + fieldName + "}");
        }
        sql.append(columns);
        return sql.toString();
    }
}
