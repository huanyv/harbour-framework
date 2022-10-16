package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;

import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/10/15 20:42
 */
public class SingleValueSqlTypeHandler extends AbstractSqlTypeHandler{

    public SingleValueSqlTypeHandler(String sql, Object[] args, Method method) {
        super(sql, args, method);
    }

    @Override
    public boolean isType() {
        Class<?> returnType = method.getReturnType();
        return Number.class.isAssignableFrom(returnType)
                || String.class.equals(returnType)
                || (returnType.isPrimitive() && !returnType.equals(void.class));
    }

    @Override
    public Object handle() {
        return SqlContextFactory.getSqlContext().selectValue(sql, args);
    }
}
