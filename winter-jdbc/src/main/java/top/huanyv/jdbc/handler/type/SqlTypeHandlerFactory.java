package top.huanyv.jdbc.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/23 10:29
 */
public class SqlTypeHandlerFactory {

    private static final List<SqlTypeHandler> SQL_TYPE_HANDLERS = new ArrayList<>();

    static {
        SQL_TYPE_HANDLERS.add(new ListSqlTypeHandler());
        SQL_TYPE_HANDLERS.add(new SingleValueSqlTypeHandler());
        SQL_TYPE_HANDLERS.add(new BeanSqlTypeHandler());
    }

    public static SqlTypeHandler getSqlTypeHandler(Method method) {
        for (SqlTypeHandler sqlTypeHandler : SQL_TYPE_HANDLERS) {
            if (sqlTypeHandler.isType(method)) {
                return sqlTypeHandler;
            }
        }
        return new BeanSqlTypeHandler();
    }
}
