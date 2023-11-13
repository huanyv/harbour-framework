package top.huanyv.bean.ioc;

import java.util.*;

/**
 * 配置类，实现这个类，并声明到IOC容器中 <br />
 * 配置类中使用{@link top.huanyv.bean.annotation.Bean @Bean}注解标识的方法，将以返回值作为实例，注入到IOC容器当中
 *
 * @author huanyv
 * @date 2023/11/6 15:46
 */
public interface Configuration {

    /**
     * 添加一条配置
     *
     * @param k 配置名
     * @param v 值
     * @return {@link Configuration}
     */
    default Configuration add(String k, String v) {
        getProperties().put(k, v);
        return this;
    }

    /**
     * 根据配置名获取一条配置值，没有相关配置返回null
     *
     * @param k 配置名
     * @return {@link String}
     */
    default String get(String k) {
        return getProperties().get(k);
    }

    /**
     * 根据配置名获取一条配置值，没有相关配置返回defaultVal
     *
     * @param k          配置名
     * @param defaultVal 默认值
     * @return {@link Object}
     */
    default String get(String k, String defaultVal) {
        String val = get(k);
        return val == null ? defaultVal : val;
    }

    default Map<String, String> getProperties() {
        return Collections.emptyMap();
    }

    /**
     * 返回所有配置名的Set集合，用于遍历
     *
     * @return {@link Set}<{@link String}>
     */
    default Set<String> getNames() {
        return Collections.unmodifiableSet(getProperties().keySet());
    }

}
