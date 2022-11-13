package top.huanyv.jdbc.handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author huanyv
 * @date 2022/9/3 20:34
 */
public class BeanHandler<T> implements ResultSetHandler<T> {

    private Class<T> type;

    private boolean mapUnderscoreToCamelCase = true;

    public BeanHandler(Class<T> type) {
        this.type = type;
    }

    public BeanHandler(Class<T> type, boolean mapUnderscoreToCamelCase) {
        this.type = type;
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        T t = null;
        try {
            if (rs.next()) {
                // 创建对象
                t = type.getConstructor().newInstance();
                // 填充对象
                populateBean(rs, t, mapUnderscoreToCamelCase);
            }
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return t;
    }
}
