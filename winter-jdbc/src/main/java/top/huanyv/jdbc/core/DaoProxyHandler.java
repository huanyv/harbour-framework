package top.huanyv.jdbc.core;

import top.huanyv.jdbc.anno.Delete;
import top.huanyv.jdbc.anno.Insert;
import top.huanyv.jdbc.anno.Select;
import top.huanyv.jdbc.anno.Update;
import top.huanyv.jdbc.handler.type.SqlTypeHandler;
import top.huanyv.jdbc.handler.type.SqlTypeHandlerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author admin
 * @date 2022/7/23 15:05
 */
public class DaoProxyHandler implements InvocationHandler {

    public DaoProxyHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        if (method.isAnnotationPresent(Select.class)) {
            return doSelect(method, args);
        }
        Update update = method.getAnnotation(Update.class);
        if (update != null) {
            return doUpdate(update.value(), method, args);
        }
        Insert insert = method.getAnnotation(Insert.class);
        if (insert != null) {
            return doUpdate(insert.value(), method, args);
        }
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null) {
            return doUpdate(delete.value(), method, args);
        }
        return null;
    }

    public Object doSelect(Method method, Object[] args) throws SQLException {
        String sql = method.getAnnotation(Select.class).value();
        SqlTypeHandler sqlTypeHandler = SqlTypeHandlerFactory.getSqlTypeHandler(method);
        return sqlTypeHandler.handle(sql, args, method);
    }

    public int doUpdate(String sql, Method method, Object[] args) throws SQLException {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql, args);
    }

}
