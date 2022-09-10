package top.huanyv.jdbc.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.huanyv.jdbc.anno.Delete;
import top.huanyv.jdbc.anno.Insert;
import top.huanyv.jdbc.anno.Select;
import top.huanyv.jdbc.anno.Update;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.MethodUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/23 15:05
 */
public class DaoProxyHandler implements InvocationHandler {

    public DaoProxyHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
        SqlContext sqlContext = SqlContextFactory.getSqlContext();

        String sql = method.getAnnotation(Select.class).value();
        Object queryResult = null;
        // 获取方法返回值类型
        Class<?> returnType = method.getReturnType();
        // 查询 list
        if (List.class.equals(returnType)) {
            // 获取list中的泛型
            Class<?> type = (Class) MethodUtil.getMethodReturnGenerics(method)[0];
            queryResult = sqlContext.selectList(type, sql, args);
        } else if(Number.class.isAssignableFrom(returnType)) {
            queryResult = sqlContext.selectValue(sql, args);
        } else if(String.class.equals(returnType)) {
            queryResult = sqlContext.selectValue(sql, args);
        } else if(returnType.isPrimitive() && !returnType.equals(void.class)) {
            queryResult = sqlContext.selectValue(sql, args);
        } else if(ClassUtil.isCustomClass(returnType)) {
            // bean 对象
            queryResult = sqlContext.selectRow(returnType, sql, args);
        }

        return queryResult;
    }

    public int doUpdate(String sql, Method method, Object[] args) throws SQLException {
        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        return sqlContext.update(sql, args);
    }

}
