package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.utils.ClassUtil;

import java.lang.reflect.Method;

/**
 * 自定义sql类型处理程序
 *
 * @author huanyv
 * @date 2022/10/15 20:45
 */
public class CustomSqlTypeHandler extends AbstractSqlTypeHandler{

    public CustomSqlTypeHandler(String sql, Object[] args, Method method) {
        super(sql, args, method);
    }

    @Override
    public boolean isType() {
        Class<?> returnType = method.getReturnType();
        return ClassUtil.isCustomClass(returnType);
    }

    @Override
    public Object handle() {
        return SqlContextFactory.getSqlContext().selectRow(method.getReturnType(), sql, args);
    }
}
