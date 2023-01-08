package top.huanyv.jdbc.handler;

import top.huanyv.tools.utils.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author huanyv
 * @date 2022/9/3 21:22
 */
public class ScalarHandler<T> implements ResultSetHandler<T> {

    private Class<T> type;

    public ScalarHandler() {
        this.type = (Class<T>) Object.class;
    }

    public ScalarHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Object result = rs.getObject(1);
            // 判断结果类型与 type 是否一致，子类即可
            Assert.isTrue(type.isInstance(result), "query result type is not "
                    + type.getSimpleName() + ", type is " + result.getClass().getSimpleName());
            return type.cast(result);
        }
        return null;
    }
}
