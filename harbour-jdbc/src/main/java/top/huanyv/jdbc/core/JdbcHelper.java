package top.huanyv.jdbc.core;

import top.huanyv.jdbc.handler.ResultSetHandler;
import top.huanyv.tools.utils.Assert;

import javax.sql.DataSource;
import java.sql.*;

/**
 * jdbc查询
 *
 * @author huanyv
 * @date 2022/9/3 20:25
 */
public class JdbcHelper {

    private DataSource dataSource;

    public JdbcHelper() {
    }

    public JdbcHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T query(String sql, ResultSetHandler<T> handler, Object... args) throws SQLException {
        Assert.notNull(dataSource, "'dataSource' must not be null.");
        return query(dataSource.getConnection(), sql, handler, args);
    }

    public <T> T query(Connection connection, String sql, ResultSetHandler<T> handler, Object... args) throws SQLException {
        Assert.notNull(connection, "'connection' must not be null.");
        Assert.notNull(sql, "'sql' must not be null.");
        Assert.notNull(handler, "'handler' must not be null.");
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            if (args != null && args.length > 0) {
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
            }
            resultSet = ps.executeQuery();
            return handler.handle(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    public int update(String sql, Object... args) throws SQLException {
        Assert.notNull(dataSource, "'dataSource' must not be null.");
        return update(dataSource.getConnection(), sql, args);
    }

    /**
     * 执行增、删、改SQL语句，返回修改成功的条数
     *
     * @param connection 连接
     * @param sql        sql
     * @param args       arg游戏
     * @return int
     * @throws SQLException sqlexception异常
     */
    public int update(Connection connection, String sql, Object... args) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            if (args != null && args.length > 0) {
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
            }
            return ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public long insert(String sql, Object... args) throws SQLException {
        Assert.notNull(dataSource, "'dataSource' must not be null.");
        return insert(dataSource.getConnection(), sql, args);
    }

    /**
     * 执行插入语句，返回自动生成的ID主键
     *
     * @param connection 连接
     * @param sql        sql
     * @param args       arg游戏
     * @return long
     * @throws SQLException sqlexception异常
     */
    public long insert(Connection connection, String sql, Object... args) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            if (args != null && args.length > 0) {
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
            }
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return -1;
    }

    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
