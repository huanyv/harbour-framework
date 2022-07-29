package top.huanyv.jdbc.extend;

import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.core.BeanRegistry;
import top.huanyv.jdbc.core.MapperScanner;
import top.huanyv.jdbc.core.SqlSession;
import top.huanyv.jdbc.core.SqlSessionFactory;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/23 16:31
 */
public class SqlSessionFactoryBean implements BeanRegistry {

    @Override
    public void set(ApplicationContext applicationContext) {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        MapperScanner mapperScanner = applicationContext.getBean(MapperScanner.class);

        SqlSession sqlSession = SqlSessionFactory.openSession(dataSource, mapperScanner);

        for (Map.Entry<String, Object> entry : sqlSession.getMapperScanner().getMappers().entrySet()) {
            String beanName = entry.getKey();
            Object beanInstance = entry.getValue();
            applicationContext.register(beanName, beanInstance);
        }

    }

}
