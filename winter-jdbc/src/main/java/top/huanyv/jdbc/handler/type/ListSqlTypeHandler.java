package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.tools.utils.MethodUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/15 20:34
 */
public class ListSqlTypeHandler implements SqlTypeHandler {

    @Override
    public Object handle(String sql, Object[] args, Method method) {
        Class<?> type = (Class) MethodUtil.getMethodReturnGenerics(method)[0];
        return SqlContextFactory.getSqlContext().selectList(type, sql, args);
    }

    @Override
    public boolean isType(Method method) {
        return List.class.isAssignableFrom(method.getReturnType());
    }
}
