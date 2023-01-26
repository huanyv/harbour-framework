package top.huanyv.jdbc.core;

import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.util.SqlHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2022/9/1 15:19
 */
public interface SqlContext {

    /**
     * 查询第一行的数据，返回type类型对象
     *
     * @param type 类型
     * @param sql  sql
     * @param args 参数
     * @return {@link T}
     */
    <T> T selectRow(Class<T> type, String sql, Object... args);

    /**
     * 查询多行数据，以type类型封装到集合中返回
     *
     * @param type 类型
     * @param sql  sql
     * @param args 参数
     * @return {@link T}
     * @return {@link List}<{@link T}>
     */
    <T> List<T> selectList(Class<T> type, String sql, Object... args);

    /**
     * 查询第一行数据，以Map集合封装
     *
     * @param sql  sql
     * @param args 参数
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> selectMap(String sql, Object... args);

    /**
     * 查询多行数据，以Map集合封装到List集合中
     *
     * @param sql  sql
     * @param args 参数
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> selectListMap(String sql, Object... args);

    /**
     * 查询一行一列（单个数值）
     *
     * @param sql  sql
     * @param args 参数
     * @return {@link Object}
     */
    Object selectValue(String sql, Object... args);

    <T> List<T> selectPage(Page<T> page, Class<T> type, String sql, Object... args);

    List<Map<String, Object>> selectPageMap(Page<Map<String, Object>> page, String sql, Object... args);

    /**
     * 查询sql结果条目数
     *
     * @param sql  sql
     * @param args 参数
     * @return long
     */
    long selectCount(String sql, Object... args);

    /**
     * 执行增加、删除、修改语句，返回操作数
     *
     * @param sql  sql
     * @param args arg游戏
     * @return int
     */
    int update(String sql, Object... args);

    /**
     * 执行插入，返回主键ID
     *
     * @param sql  sql
     * @param args 参数
     * @return long
     */
    long insert(String sql, Object... args);

    default <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler) {
        return sqlExecute(sql, args, handler, null);
    }

    /**
     * 处理sql，过程由SqlHandler接口实现，
     *
     * @param sql      sql
     * @param args     参数
     * @param handler  处理程序
     * @param exReturn 异常返回值
     * @return {@link T}
     */
    <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler, T exReturn);

    /**
     * 打开事务
     */
    void beginTransaction();

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

    /**
     * 关闭连接
     */
    void close();

    /**
     * 根据类型，获取动态代理后的对象
     *
     * @param type 类型
     * @param <T>  类型泛型
     * @return 代理
     */
    <T> T getDao(Class<T> type);

    /**
     * 获得连接
     *
     * @return {@link Connection}
     * @throws SQLException sqlexception异常
     */
    Connection getConnection() throws SQLException;

}
