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

    public BeanHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        T t = null;
        try {
            if (rs.next()) {
                // 创建对象
                t = type.getConstructor().newInstance();
                // 填充对象
                populateBean(rs, t);
            }
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return t;
    }
}
