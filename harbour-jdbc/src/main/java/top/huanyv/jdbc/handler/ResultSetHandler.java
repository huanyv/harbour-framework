package top.huanyv.jdbc.handler;

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

    default void populateBean(ResultSet rs, Object t, boolean mapUnderscoreToCamelCase) {
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object val = null;
            try {
                String columnName = field.getName();
                if (mapUnderscoreToCamelCase) {
                    columnName = StringUtil.camelCaseToUnderscore(columnName);
                }
                val = rs.getObject(columnName);
            } catch (SQLException throwables) {
                val = null;
            }
            if (val != null) {
                try {
                    field.set(t, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
