package top.huanyv.jdbc.core;

import com.alibaba.druid.util.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/22 17:20
 */
public abstract class GeneralCRUD {

    private QueryRunner queryRunner = new QueryRunner();

    public int update(Connection conn, String sql, Object... agrs) {
        try {
            int update = queryRunner.update(conn, sql, agrs);
            return update;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn);
        }
        return -1;
    }

    public <T> T queryForOne(Connection conn, Class<T> type, String sql, Object... args) {
        try {
            T query = queryRunner.query(conn, sql, new BeanHandler<T>(type), args);
            return query;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
        }
        return null;
    }

    public <T> List<T> queryForList(Connection conn, Class<T> type, String sql, Object... args) {
        try {
            List<T> query = queryRunner.query(conn, sql, new BeanListHandler<T>(type), args);
            return query;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
        }
        return null;
    }

    public Object queryForOneValue(Connection conn, String sql,Object... args) {
        try {
            Object query = queryRunner.query(conn,sql, new ScalarHandler(), args);
            return query;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
        }
        return null;
    }

}
