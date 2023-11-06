package top.huanyv.jdbc.util;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.bean.utils.ReflectUtil;
import top.huanyv.bean.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 * @author huanyv
 * @date 2023/2/24 16:26
 */
public final class SqlUtil {

    private SqlUtil() {
    }

    public static String generateInsert(Class<?> cls) {
        return generateInsert(cls, true);
    }

    public static String generateInsert(Class<?> cls, boolean u2c) {
        StringBuilder sql = new StringBuilder("insert into ").append(StringUtil.firstLetterLower(cls.getSimpleName()));
        // (column1, column2, column3, .....)
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        // (?, ?, ?, ......)
        StringJoiner values = new StringJoiner(", ", "(", ")");

        for (Field field : ReflectUtil.getAllDeclaredFields(cls)) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // 驼峰转下划线
            String columnName = u2c ? StringUtil.camelCaseToUnderscore(fieldName) : fieldName;
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

    public static String generateUpdate(Class<?> cls, boolean u2c) {
        StringBuilder sql = new StringBuilder("update ").append(StringUtil.firstLetterLower(cls.getSimpleName())).append(" set ");
        // column1 = ?, column2 = ?, ......
        StringJoiner columns = new StringJoiner(", ");
        for (Field field : ReflectUtil.getAllDeclaredFields(cls)) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // 驼峰转下划线
            String columnName = u2c ? StringUtil.camelCaseToUnderscore(fieldName) : fieldName;
            // 自定义列名
            Column column = field.getAnnotation(Column.class);
            columnName = column != null ? column.value() : columnName;
            columns.add(columnName + " = #{" + fieldName + "}");
        }
        sql.append(columns);
        return sql.toString();
    }

    public static String generateUpdateDynamicCode(Class<?> cls, String oName) {
        return generateUpdateDynamicCode(cls, oName, true);
    }
    public static String generateUpdateDynamicCode(Class<?> cls, String oName, boolean u2c) {
        StringBuilder sb = new StringBuilder("SqlBuilder sb = new SqlBuilder(\"update ");
        sb.append(StringUtil.firstLetterLower(cls.getSimpleName())).append(" set").append("\")");
        sb.append("\n\t.join(\", \", join -> join");
        for (Field field : ReflectUtil.getAllDeclaredFields(cls)) {
            String fieldName = field.getName();
            if (String.class.equals(field.getType())) {
                sb.append("\n\t\t.append(StringUtil.hasText(").append(oName);
            } else {
                sb.append("\n\t\t.append(Objects.nonNull(").append(oName);
            }
            sb.append(".get").append(StringUtil.firstLetterUpper(fieldName)).append("()");
            sb.append("), \"").append(u2c ? StringUtil.camelCaseToUnderscore(fieldName) : fieldName).append(" = #{").append(fieldName).append("}\")");
        }
        sb.append("\n\t)");
        return sb.toString();
    }

}
