package top.huanyv.jdbc.handler.type;

import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.util.PageHolder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义sql类型处理程序
 *
 * @author huanyv
 * @date 2022/10/15 20:45
 */
public class PageSqlTypeHandler implements SqlTypeHandler {

    @Override
    public Object handle(String sql, Object[] args, Method method) {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        // 获取Page对象
        Page page = PageHolder.getPage();
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            Type type = parameterizedType.getActualTypeArguments()[0];
            sqlContext.selectPage(page, (Class) type, sql, args);
            return page;
        }
        sqlContext.selectPageMap(page, sql ,args);
        return page;
    }

    @Override
    public boolean isType(Method method) {
        return Page.class.equals(method.getReturnType());
    }

}
