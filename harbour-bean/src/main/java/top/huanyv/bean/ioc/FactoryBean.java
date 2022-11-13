package top.huanyv.bean.ioc;

/**
 * 工厂bean
 *
 * @author huanyv
 * @date 2022/10/22 16:33
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
