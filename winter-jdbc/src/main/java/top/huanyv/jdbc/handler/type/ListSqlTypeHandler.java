package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.utils.MethodUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/15 20:34
 */
public class ListSqlTypeHandler extends AbstractSqlTypeHandler{

    public ListSqlTypeHandler(String sql, Object[] args, Method method) {
        super(sql, args, method);
    }

    @Override
    public Object handle() {
        Class<?> type = (Class) MethodUtil.getMethodReturnGenerics(method)[0];
        return SqlContextFactory.getSqlContext().selectList(type, this.sql, this.args);
    }

    @Override
    public boolean isType() {
        return List.class.isAssignableFrom(method.getReturnType());
    }
}
