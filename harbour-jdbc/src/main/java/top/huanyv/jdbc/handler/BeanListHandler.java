package top.huanyv.jdbc.handler;

import top.huanyv.tools.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/9/3 20:46
 */
public class BeanListHandler<T> extends AbstractBeanHandler<List<T>> {

    private Class<T> type;

    private boolean mapUnderscoreToCamelCase = true;

    public BeanListHandler(Class<T> type) {
        Assert.notNull(type, "'type' must not be null.");
        this.type = type;
    }

    public BeanListHandler(Class<T> type, boolean mapUnderscoreToCamelCase) {
        this(type);
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        T t = null;
        try {
            while (rs.next()) {
                // 创建实例
                t = type.getConstructor().newInstance();
                // 填充
                populateBean(rs, t, mapUnderscoreToCamelCase);
                // 到list
                result.add(t);
            }
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

}
