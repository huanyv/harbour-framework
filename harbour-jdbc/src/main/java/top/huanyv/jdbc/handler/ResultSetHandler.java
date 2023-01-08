package top.huanyv.jdbc.handler;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理器
 *
 * @author huanyv
 * @date 2022/9/3 20:26
 */
public interface ResultSetHandler<T> {

    T handle(ResultSet rs) throws SQLException;

    /**
     * ResultSet中的属性填充到对应对象中
     *
     * @param rs                       rs
     * @param t                        t
     * @param mapUnderscoreToCamelCase 强调映射到驼峰式大小写
     */
    default void populateBean(ResultSet rs, Object t, boolean mapUnderscoreToCamelCase) {
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String columnName = field.getName();
                // 开启驼峰映射
                if (mapUnderscoreToCamelCase) {
                    columnName = StringUtil.camelCaseToUnderscore(columnName);
                }
                Column column = field.getAnnotation(Column.class);
                columnName = column != null ? column.value() : columnName;
                Object val = rs.getObject(columnName);
                if (val != null) {
                    field.set(t, val);
                }
            } catch (SQLException | IllegalAccessException e) {
            }
        }
    }

}
