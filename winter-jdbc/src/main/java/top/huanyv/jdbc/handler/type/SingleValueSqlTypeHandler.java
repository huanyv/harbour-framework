package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;

import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2022/10/15 20:42
 */
public class SingleValueSqlTypeHandler implements SqlTypeHandler {


    @Override
    public Object handle(String sql, Object[] args, Method method) {
        return SqlContextFactory.getSqlContext().selectValue(sql, args);
    }

    @Override
    public boolean isType(Method method) {
        Class<?> returnType = method.getReturnType();
        return Number.class.isAssignableFrom(returnType)
                || String.class.equals(returnType)
                || (returnType.isPrimitive() && !returnType.equals(void.class));
    }

}
