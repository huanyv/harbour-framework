package top.huanyv.jdbc.core;

import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.util.SqlHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/1/11 14:44
 */
public class SqlContextManager implements SqlContext {

    private final SqlContext sqlContextProxy;

    public SqlContextManager() {
        this.sqlContextProxy = (SqlContext) Proxy.newProxyInstance(SqlContext.class.getClassLoader(),
                new Class[]{SqlContext.class},
                new SqlContextInvocationHandler());
    }

    @Override
    public <T> T selectRow(Class<T> type, String sql, Object... args) {
        return this.sqlContextProxy.selectRow(type, sql, args);
    }

    @Override
    public <T> List<T> selectList(Class<T> type, String sql, Object... args) {
        return this.sqlContextProxy.selectList(type, sql, args);
    }

    @Override
    public Map<String, Object> selectMap(String sql, Object... args) {
        return this.sqlContextProxy.selectMap(sql, args);
    }

    @Override
    public List<Map<String, Object>> selectListMap(String sql, Object... args) {
        return this.sqlContextProxy.selectListMap(sql, args);
    }

    @Override
    public Object selectValue(String sql, Object... args) {
        return this.sqlContextProxy.selectValue(sql, args);
    }

    @Override
    public <T> List<T> selectPage(Page<T> page, Class<T> type, String sql, Object... args) {
        return this.sqlContextProxy.selectPage(page, type, sql, args);
    }

    @Override
    public List<Map<String, Object>> selectPageMap(Page<Map<String, Object>> page, String sql, Object... args) {
        return this.sqlContextProxy.selectPageMap(page, sql, args);
    }

    @Override
    public long selectCount(String sql, Object... args) {
        return this.sqlContextProxy.selectCount(sql, args);
    }

    @Override
    public int update(String sql, Object... args) {
        return this.sqlContextProxy.update(sql, args);
    }

    @Override
    public long insert(String sql, Object... args) {
        return this.sqlContextProxy.insert(sql, args);
    }

    @Override
    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler) {
        return this.sqlContextProxy.sqlExecute(sql, args, handler);
    }

    @Override
    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler, T exReturn) {
        return this.sqlContextProxy.sqlExecute(sql, args, handler, exReturn);
    }

    @Override
    public void beginTransaction() {
        this.sqlContextProxy.beginTransaction();
    }

    @Override
    public void commit() {
        this.sqlContextProxy.commit();
    }

    @Override
    public void rollback() {
        this.sqlContextProxy.rollback();
    }

    @Override
    public void closeTransaction() {
        this.sqlContextProxy.closeTransaction();
    }

    @Override
    public <T> T getDao(Class<T> type) {
        return this.sqlContextProxy.getDao(type);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.sqlContextProxy.getConnection();
    }

    public static class SqlContextInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SqlContext sqlContext = SqlContextFactory.getSqlContext();
            return method.invoke(sqlContext, args);
        }
    }

}
