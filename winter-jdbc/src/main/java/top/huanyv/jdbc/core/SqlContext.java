package top.huanyv.jdbc.core;

import com.sun.tracing.dtrace.ArgsAttributes;
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

    private Configuration config = Configuration.create();

    private boolean isAutoClose = true;

    public SqlContext() {
        this.dataSource = config.getDataSource();
    }

    public SqlContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> selectList(Class<T> type, String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new BeanListHandler<T>(type), args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    public <T> T selectRow(Class<T> type, String sql, Object... args) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new BeanHandler<T>(type), args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    public Object selectValue(String sql, Object... args) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            return queryRunner.query(conn, sql, new ScalarHandler<>(), args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (isAutoClose && conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit() {
        try {
            this.connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() {
        try {
            this.connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = this.dataSource.getConnection();
        }
        return this.connection;
    }

}
