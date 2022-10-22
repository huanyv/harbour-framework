package top.huanyv.jdbc.extend;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.core.ApplicationContextWeave;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextFactory;
import top.huanyv.utils.ClassLoaderUtil;

import java.io.InputStream;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/23 16:31
 */
public class SqlContextFactoryBean implements ApplicationContextWeave {

    // 配置文件名
    private String configLocation;

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    public void populateBeanBefore(ApplicationContext applicationContext) {
        if (configLocation != null) {
            InputStream inputStream = ClassLoaderUtil.getInputStream(configLocation);
            // 加载配置
            JdbcConfigurer.create(inputStream);
        }

        SqlContext sqlContext = SqlContextFactory.getSqlContext();
        // bean注册
        for (Map.Entry<String, Object> entry : sqlContext.getDaos().entrySet()) {
            String beanName = entry.getKey();
            Object beanInstance = entry.getValue();
            applicationContext.registerBean(beanName, beanInstance);
        }
    }

}
