package top.huanyv.jdbc.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.huanyv.jdbc.anno.Dao;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<String, Object> daos = new ConcurrentHashMap<>();

    public SqlContext() {
        this.dataSource = config.getDataSource();
        String scanPackages = config.getScanPackages();
        // 扫描包
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(scanPackages, Dao.class);
        for (Class<?> clazz : classes) {
            // 接口名首字母小写
            String mapperName = StringUtil.firstLetterLower(clazz.getSimpleName());
            Object mapperInstance = null;
            if (clazz.isInterface()) {
                // 代理实现
                mapperInstance = ProxyFactory.getImpl(clazz, new MapperProxyHandler(this));
            } else {
                try {
                    mapperInstance = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | IllegalAccessException
                        | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (StringUtil.hasText(mapperName) && mapperInstance != null) {
                this.daos.put(mapperName, mapperInstance);
            }
        }
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

    public <T> T selectRow(Class<T> type, String sql, Object... args) {
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

    public Object selectValue(String sql, Object... args) {
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

    /**
     * 根据类型，获取动态代理后的对象
     * @param type 类型
     * @param <T> 类型泛型
     * @return 代理
     */
    public <T> T getDao(Class<T> type) {
        for (Map.Entry<String, Object> entry : daos.entrySet()) {
            Object mapper = entry.getValue();
            if (type.isInstance(mapper)) {
                return (T) mapper;
            }
        }
        return null;
    }

    /**
     * 获取所有的dao 实例
     * @return map key为name, value为dao对象
     */
    public Map<String, Object> getDaos() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.daos.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = this.dataSource.getConnection();
        }
        return this.connection;
    }

}
