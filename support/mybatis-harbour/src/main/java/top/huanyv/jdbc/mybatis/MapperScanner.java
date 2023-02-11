package top.huanyv.jdbc.mybatis;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import top.huanyv.bean.ioc.BeanDefinitionRegistry;
import top.huanyv.bean.ioc.BeanDefinitionRegistryPostProcessor;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.tools.utils.ClassLoaderUtil;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.StringUtil;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2022/12/22 17:01
 */
public class MapperScanner implements BeanDefinitionRegistryPostProcessor {

    /**
     * 全局配置文件
     */
    private String configLocation;

    /**
     * Mapper接口所在的包，生成代理类放到IOC中，会自动扫描Mapper接口，自动绑定
     */
    private String basePackage;

    /**
     * 三方数据源
     */
    private DataSource dataSource;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        Set<Class<?>> classes = ClassUtil.getClasses(basePackage);
        InputStream inputStream = ClassLoaderUtil.getInputStream(configLocation);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 获取mybatis配置信息
        Configuration configuration = sqlSessionFactory.getConfiguration();

        // 绑定Mapper接口与文件，只绑定没有配置的
        Set<String> mapperPackages = configuration.getMapperRegistry().getMappers()
                .stream().map(cls -> cls.getPackage().getName()) .collect(Collectors.toSet());
        if (!mapperPackages.contains(basePackage)) {
            configuration.addMappers(basePackage);
        }

        // 如果使用了三方数据源
        if (dataSource != null) {
            Environment oldEnv = configuration.getEnvironment();
            TransactionFactory transactionFactory = oldEnv.getTransactionFactory();
            String envId = oldEnv.getId();
            Environment newEnv = new Environment(envId, transactionFactory, dataSource);
            configuration.setEnvironment(newEnv);
        }
        for (Class<?> cls : classes) {
            BeanDefinition beanDefinition = new ClassBeanDefinition(MapperFactoryBean.class, cls, sqlSessionFactory);
            registry.register(StringUtil.firstLetterLower(cls.getSimpleName()), beanDefinition);
        }
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
