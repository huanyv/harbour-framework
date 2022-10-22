package top.huanyv.ioc.core;

/**
 * 工厂bean
 *
 * @author huanyv
 * @date 2022/10/22 16:33
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

}
