package top.huanyv.jdbc.support;

import top.huanyv.ioc.core.FactoryBean;
import top.huanyv.jdbc.core.SqlContextFactory;

/**
 * @author admin
 * @date 2022/7/23 16:31
 */
public class SqlContextFactoryBean<T> implements FactoryBean<T> {

    private Class<T> daoInterface;

    public SqlContextFactoryBean(Class<T> daoInterface) {
        this.daoInterface = daoInterface;
    }

    @Override
    public T getObject() {
        return SqlContextFactory.getSqlContext().getDao(daoInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return daoInterface;
    }

}
