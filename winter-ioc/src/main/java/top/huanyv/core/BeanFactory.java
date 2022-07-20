package top.huanyv.core;

/**
 * @author admin
 * @date 2022/7/19 17:49
 */
public interface BeanFactory {

    /**
     * 根据名称获取bean实例
     * @param beanName bean名称
     * @return bean
     */
    Object getBean(String beanName);


    /**
     * 根据类型获取bean
     * @param clazz 类型
     * @param <T>类型
     * @return bean
     */
    <T> T getBean(Class<T> clazz);


    /**
     * 是否包含一个bean
     * @param name bean名称
     * @return boolean
     */
    boolean containsBean(String name);

}
