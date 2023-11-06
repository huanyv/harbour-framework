package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.bean.utils.BeanUtil;

import java.lang.reflect.Method;

/**
 * 自定义sql类型处理程序
 *
 * @author huanyv
 * @date 2022/10/15 20:45
 */
public class BeanSqlTypeHandler implements SqlTypeHandler{

    @Override
    public Object handle(String sql, Object[] args, Method method) {
        return SqlContextFactory.getSqlContext().selectRow(method.getReturnType(), sql, args);
    }

    @Override
    public boolean isType(Method method) {
        Class<?> returnType = method.getReturnType();
        return BeanUtil.isJavaBean(returnType);
    }

}
