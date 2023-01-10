package top.huanyv.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author huanyv
 * @date 2023/1/9 15:49
 */
@FunctionalInterface
public interface SqlHandler<T> {
    T handle(Connection connection, SqlAndArgs sqlAndArgs) throws SQLException;
}
