package top.huanyv.jdbc.core;

import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.util.SqlHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/1/11 14:44
 */
public class SqlContextManager implements SqlContext {

    @Override
    public <T> T selectRow(Class<T> type, String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectRow(type, sql, args);
    }

    @Override
    public <T> List<T> selectList(Class<T> type, String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectList(type, sql, args);
    }

    @Override
    public Map<String, Object> selectMap(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectMap(sql, args);
    }

    @Override
    public List<Map<String, Object>> selectListMap(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectListMap(sql, args);
    }

    @Override
    public Object selectValue(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectValue(sql, args);
    }

    @Override
    public <T> List<T> selectPage(Page<T> page, Class<T> type, String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectPage(page, type, sql, args);
    }

    @Override
    public List<Map<String, Object>> selectPageMap(Page<Map<String, Object>> page, String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectPageMap(page, sql, args);
    }

    @Override
    public long selectCount(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().selectCount(sql, args);
    }

    @Override
    public int update(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().update(sql, args);
    }

    @Override
    public long insert(String sql, Object... args) {
        return SqlContextFactory.getSqlContext().insert(sql, args);
    }

    @Override
    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler) {
        return SqlContextFactory.getSqlContext().sqlExecute(sql, args, handler);
    }

    @Override
    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler, T exReturn) {
        return SqlContextFactory.getSqlContext().sqlExecute(sql, args, handler, exReturn);
    }

    @Override
    public void beginTransaction() {
        SqlContextFactory.getSqlContext().beginTransaction();
    }

    @Override
    public void commit() {
        SqlContextFactory.getSqlContext().commit();
    }

    @Override
    public void rollback() {
        SqlContextFactory.getSqlContext().rollback();
    }

    @Override
    public void closeTransaction() {
        SqlContextFactory.getSqlContext().closeTransaction();
    }

    @Override
    public <T> T getDao(Class<T> type) {
        return SqlContextFactory.getSqlContext().getDao(type);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return SqlContextFactory.getSqlContext().getConnection();
    }

}
