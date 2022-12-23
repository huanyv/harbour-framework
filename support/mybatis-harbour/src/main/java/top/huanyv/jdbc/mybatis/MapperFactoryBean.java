package top.huanyv.jdbc.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import top.huanyv.bean.ioc.FactoryBean;

/**
 * @author huanyv
 * @date 2022/12/22 16:51
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> mapperInterface;

    private final SqlSessionDecorator sqlSessionDecorator;

    public MapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionDecorator = new SqlSessionDecorator(sqlSessionFactory);
    }

    @Override
    public T getObject() throws Exception {
        return sqlSessionDecorator.getMapper(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }
}
