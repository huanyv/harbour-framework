package top.huanyv.start.loader;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.datasource.SimpleDataSource;
import top.huanyv.jdbc.support.DaoScanner;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;

/**
 * TODO BUG 不会织入
 *
 * @author huanyv
 * @date 2022/12/19 13:53
 */
@ConfigurationProperties(prefix = "jdbc")
public class JdbcStartLoader implements ApplicationLoader {

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String scanPackages;

    @Override
    public void load(ApplicationContext applicationContext, AppArguments appArguments) {
        // 加载配置
        JdbcConfigurer jdbcConfigurer = JdbcConfigurer.create();

        SimpleDataSource simpleDataSource = new SimpleDataSource();
        simpleDataSource.setUrl(url);
        simpleDataSource.setDriverClassName(driverClassName);
        simpleDataSource.setUsername(username);
        simpleDataSource.setPassword(password);

        jdbcConfigurer.setDataSource(simpleDataSource);
        jdbcConfigurer.setScanPackages(scanPackages);
    }

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public DaoScanner daoScanner() {
        return new DaoScanner();
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, DaoScanner.class);
        }
    }
}
