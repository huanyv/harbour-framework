package top.huanyv.jdbc.core.datasource;

import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.StringUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源
 *
 * @author huanyv
 * @date 2023/1/9 14:34
 */
public class DynamicDatasource extends AbstractDataSource {

    public static final String DEFAULT_KEY = "default";

    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getDataSource().getConnection(username, password);
    }

    public DataSource getDataSource() {
        String dataSourceKey = getDataSourceKey();
        if (StringUtil.hasText(dataSourceKey)) {
            return this.dataSourceMap.get(dataSourceKey);
        }
        return getDefaultDataSource();
    }

    public DataSource getDataSource(String key) {
        return this.dataSourceMap.get(key);
    }

    public void setDataSource(String key, DataSource dataSource) {
        this.dataSourceMap.put(key, dataSource);
    }

    public DataSource getDefaultDataSource() {
        return this.dataSourceMap.get(DEFAULT_KEY);
    }

    public void setDefaultDataSource(DataSource dataSource) {
        Assert.notNull(dataSource, "'dataSource' must not be null.");
        this.dataSourceMap.put(DEFAULT_KEY, dataSource);
    }

    public String getDataSourceKey() {
        return DataSourceKeyHolder.get();
    }

}
