package top.huanyv.start.loader.jdbc;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.support.DaoScanner;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.bean.utils.Assert;

/**
 * @author huanyv
 * @date 2022/12/19 13:53
 */
@Properties(prefix = "harbour.jdbc.")
public class JdbcStartLoader implements ApplicationLoader {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String scanPackages;

    /**
     * 是否开启下线线转驼峰
     */
    private boolean u2c = true;

    @Override
    public void load(ApplicationContext applicationContext, Configuration configuration) {
        Assert.notNull(driverClassName, "database 'driverClassName' property not configured!");
        Assert.notNull(url, "database 'url' property not configured!");
        Assert.notNull(username, "database 'username' property not configured!");

        // 加载配置
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl(url);
        simpleDataSource.setDriverClassName(driverClassName);
        simpleDataSource.setUsername(username);
        simpleDataSource.setPassword(password == null ? "" : password);

        jdbcConfigurer.setDataSource(simpleDataSource);
        // 是否开启下线线转驼峰
        jdbcConfigurer.setMapUnderscoreToCamelCase(u2c);
    }

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public DaoScanner daoScanner() {
        Assert.notNull(scanPackages, "'scanPackages' property not configured!");
        return new DaoScanner(scanPackages);
    }

    public static class ConditionOnMissingBean implements Condition {
        @Override
        public boolean matchers(ApplicationContext applicationContext, Configuration configuration) {
            return BeanFactoryUtil.isNotPresent(applicationContext, DaoScanner.class);
        }
    }
}
