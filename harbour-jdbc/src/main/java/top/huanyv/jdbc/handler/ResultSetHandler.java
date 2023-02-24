package top.huanyv.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理器
 *
 * @author huanyv
 * @date 2022/9/3 20:26
 */
public interface ResultSetHandler<T> {

    T handle(ResultSet rs) throws SQLException;

}
