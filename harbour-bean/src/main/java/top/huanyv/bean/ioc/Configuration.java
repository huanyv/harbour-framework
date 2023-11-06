package top.huanyv.bean.ioc;

/**
 * 配置类
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
        return this;
    }

    /**
     * 根据配置名获取一条配置值，没有相关配置返回null
     *
     * @param k 配置名
     * @return {@link String}
     */
    default String get(String k) {
        return null;
    }

    /**
     * 根据配置名获取一条配置值，没有相关配置返回defaultVal
     *
     * @param k          配置名
     * @param defaultVal 默认值
     * @return {@link Object}
     */
    default Object get(String k, String defaultVal) {
        String val = get(k);
        return val == null ? defaultVal : val;
    }

}
