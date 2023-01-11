package top.huanyv.jdbc.core;

import top.huanyv.jdbc.handler.ResultSetHandler;

import java.sql.*;

/**
 * jdbc模板
 *
 * @author huanyv
 * @date 2022/9/3 20:25
 */
public class JdbcTemplate {

    public JdbcTemplate() {
    }

    public <T> T query(Connection connection, String sql, ResultSetHandler<T> resultSetHandler, Object... args) throws SQLException {
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
            return resultSetHandler.handle(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
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

}
