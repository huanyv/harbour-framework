package top.huanyv.jdbc.core;

import top.huanyv.jdbc.core.proxy.ClassDaoProxyHandler;
import top.huanyv.jdbc.core.proxy.InterfaceDaoProxyHandler;
import top.huanyv.jdbc.core.proxy.ProxyFactory;
import top.huanyv.jdbc.handler.*;
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
public class SqlContext {
    private DataSource dataSource;
    private Connection connection;

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

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
            return jdbcTemplate.query(conn, sql, new BeanListHandler<T>(type), args);
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
            return jdbcTemplate.query(conn, sql, new BeanHandler<T>(type), args);
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

    public Map<String, Object> selectMap(String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return jdbcTemplate.query(conn, sql, new MapHandler(), args);
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

    public List<Map<String, Object>> selectListMap(String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return jdbcTemplate.query(conn, sql, new MapListHandler(), args);
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
            return jdbcTemplate.query(conn, sql, new ScalarHandler<>(), args);
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
            return jdbcTemplate.update(conn, sql, args);
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
     * 执行插入，返回主键ID
     *
     * @param sql  sql
     * @param args 参数
     * @return long
     */
    public long insert(String sql, Object... args) {
        Connection conn = null;
        try {
            conn = getConnection();
            return jdbcTemplate.insert(conn, sql, args);
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
        return -1;
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
            throw new IllegalArgumentException("'" + type + "' is a abstract class!");
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
