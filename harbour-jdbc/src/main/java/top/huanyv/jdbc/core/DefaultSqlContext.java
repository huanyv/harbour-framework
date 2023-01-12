package top.huanyv.jdbc.core;

import top.huanyv.jdbc.core.proxy.ClassDaoProxyHandler;
import top.huanyv.jdbc.core.proxy.InterfaceDaoProxyHandler;
import top.huanyv.jdbc.core.proxy.ProxyFactory;
import top.huanyv.jdbc.handler.*;
import top.huanyv.jdbc.util.Page;
import top.huanyv.jdbc.util.SqlAndArgs;
import top.huanyv.jdbc.util.SqlHandler;
import top.huanyv.jdbc.util.SqlParamParser;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.ReflectUtil;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author huanyv
 * @date 2022/9/1 15:19
 */
public class DefaultSqlContext implements SqlContext {

    private DataSource dataSource;

    private Connection connection;

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    // 是否自动关闭连接，事务开启后会 false
    private boolean isAutoClose = true;

    public DefaultSqlContext() {
        // 配置类
        JdbcConfigurer config = JdbcConfigurer.create();
        this.dataSource = config.getDataSource();
    }

    public <T> T selectRow(Class<T> type, String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.query(connection, sqlAndArgs.getSql(), new BeanHandler<>(type), sqlAndArgs.getArgs()));
    }

    public <T> List<T> selectList(Class<T> type, String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.query(connection, sqlAndArgs.getSql(), new BeanListHandler<>(type), sqlAndArgs.getArgs()));
    }

    public Map<String, Object> selectMap(String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.query(connection, sqlAndArgs.getSql(), new MapHandler(), sqlAndArgs.getArgs()));
    }

    public List<Map<String, Object>> selectListMap(String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.query(connection, sqlAndArgs.getSql(), new MapListHandler(), sqlAndArgs.getArgs()));
    }

    public Object selectValue(String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.query(connection, sqlAndArgs.getSql(), new ScalarHandler<>(), sqlAndArgs.getArgs()));
    }

    public <T> List<T> selectPage(Page<T> page, Class<T> type, String sql, Object... args) {
        Assert.notNull(page, "'page' must not be null.");
        long total = selectCount(sql, args);
        page.setTotal(total);
        sql = page.getPageSql(sql);
        List<T> list = selectList(type, sql, args);
        page.setData(list);
        return list;
    }

    public List<Map<String, Object>> selectPageMap(Page<Map<String, Object>> page, String sql, Object... args) {
        Assert.notNull(page, "'page' must not be null.");
        long total = selectCount(sql, args);
        page.setTotal(total);
        sql = page.getPageSql(sql);
        List<Map<String, Object>> mapList = selectListMap(sql, args);
        page.setData(mapList);
        return mapList;
    }

    /**
     * sql结果条目数
     *
     * @param sql  sql
     * @param args 参数
     * @return long
     */
    public long selectCount(String sql, Object... args) {
        return (long) selectValue("select count(*) from (" + sql + ") t1", args);
    }

    /**
     * 执行增加、删除、修改语句
     *
     * @param sql  sql
     * @param args arg游戏
     * @return int
     */
    public int update(String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.update(connection, sqlAndArgs.getSql(), sqlAndArgs.getArgs()), 0);
    }

    /**
     * 执行插入，返回主键ID
     *
     * @param sql  sql
     * @param args 参数
     * @return long
     */
    public long insert(String sql, Object... args) {
        return sqlExecute(sql, args, (connection, sqlAndArgs) ->
                jdbcTemplate.insert(connection, sqlAndArgs.getSql(), sqlAndArgs.getArgs()), -1L);
    }

    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler) {
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
    public <T> T sqlExecute(String sql, Object[] args, SqlHandler<T> handler, T exReturn) {
        Connection connection = null;
        try {
            connection = getConnection();
            // 解析参数化SQL与参数
            SqlAndArgs sqlAndArgs = SqlParamParser.parse(sql, args);
            return handler.handle(connection, sqlAndArgs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (isAutoClose && connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return exReturn;
    }

    /**
     * 打开事务
     */
    public void beginTransaction() {
        try {
            // 关闭 自动关闭链接
            isAutoClose = false;
            // 确保连接不为null
            getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭事务
     */
    public void closeTransaction() {
        if (!isAutoClose) {
            isAutoClose = true;
            try {
                this.connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据类型，获取动态代理后的对象
     *
     * @param type 类型
     * @param <T>  类型泛型
     * @return 代理
     */
    public <T> T getDao(Class<T> type) {
        if (type.isInterface()) {
            return ProxyFactory.getImpl(type, new InterfaceDaoProxyHandler());
        }
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("'type' must not be abstract class.");
        }
        return ProxyFactory.getProxy(type, new ClassDaoProxyHandler(ReflectUtil.newInstance(type)));
    }

    /**
     * 获得连接
     *
     * @return {@link Connection}
     * @throws SQLException sqlexception异常
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = this.dataSource.getConnection();
        }
        return this.connection;
    }

}
