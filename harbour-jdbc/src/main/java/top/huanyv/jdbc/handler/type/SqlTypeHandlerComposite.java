package top.huanyv.jdbc.handler.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2023/1/9 13:25
 */
public class SqlTypeHandlerComposite implements SqlTypeHandler {

    private final List<SqlTypeHandler> handlers = new ArrayList<>();

    public SqlTypeHandlerComposite() {
        handlers.add(new ListSqlTypeHandler());
        handlers.add(new SingleValueSqlTypeHandler());
        handlers.add(new BeanSqlTypeHandler());
    }

    @Override
    public Object handle(String sql, Object[] args, Method method) {
        SqlTypeHandler handler = getHandler(method);
        return handler != null ? handler.handle(sql, args, method) : null;
    }

    @Override
    public boolean isType(Method method) {
        return getHandler(method) != null;
    }

    public SqlTypeHandler getHandler(Method method) {
        for (SqlTypeHandler handler : handlers) {
            if (handler.isType(method)) {
                return handler;
            }
        }
        return null;
    }

}
