package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.bean.utils.BeanUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/10/15 20:34
 */
public class ListSqlTypeHandler implements SqlTypeHandler {

    @Override
    public Object handle(String sql, Object[] args, Method method) {
        Type type = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        return SqlContextFactory.getSqlContext().selectList((Class<?>) type, sql, args);
    }

    @Override
    public boolean isType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        // 不是泛型类型
        if (!(genericReturnType instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType returnType = (ParameterizedType) genericReturnType;
        // 原类型不是List
        Type rawType = returnType.getRawType();
        if (!(rawType instanceof Class)) {
            return false;
        }
        if (!List.class.isAssignableFrom((Class<?>) rawType)) {
            return false;
        }
        // 具体类型不是JavaBean
        Type actualTypeArgument = returnType.getActualTypeArguments()[0];
        if (!(actualTypeArgument instanceof Class)) {
            return false;
        }
        if (!BeanUtil.isJavaBean((Class<?>) actualTypeArgument)) {
            return false;
        }
        return true;
    }
}
