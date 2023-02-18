package top.huanyv.jdbc.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyv
 * @date 2022/9/4 14:57
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>>{
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        if (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object val = rs.getObject(columnName);
                result.put(columnName, val);
            }
        }
        return result;
    }
}
