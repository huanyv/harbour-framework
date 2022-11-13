package top.huanyv.jdbc.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/9/1 15:19
 */
public class SqlContext {
    private DataSource dataSource;
    private Connection connection;

    private QueryRunner queryRunner = new QueryRunner();

    // 配置类
    private JdbcConfigurer config = JdbcConfigurer.create();

    // 是否自动关闭连接，事务开启后会 false
    private boolean isAutoClose = true;

    public SqlContext() {
        this.dataSource = config.getDataSource();
    }

    public <T> List<T> selectList(Class<T> type, String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new BeanListHandler<T>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public <T> T selectRow(Class<T> type, String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new BeanHandler<T>(type), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Object selectValue(String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new ScalarHandler<>(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public int update(String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.update(conn, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
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
     * 根据类型，获取动态代理后的对象
     * @param type 类型
     * @param <T> 类型泛型
     * @return 代理
     */
    public <T> T getDao(Class<T> type) {
        return ProxyFactory.getImpl(type, new DaoProxyHandler());
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
