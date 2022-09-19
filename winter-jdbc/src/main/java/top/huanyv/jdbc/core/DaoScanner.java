package top.huanyv.jdbc.core;

import top.huanyv.jdbc.anno.Dao;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @date 2022/7/22 15:54
 */
public class DaoScanner {

    // 所有的dao接口代理实例
    private Map<String, Object> daos = new ConcurrentHashMap<>();

    public DaoScanner(String... scanPackages) {
        // 扫描包
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Dao.class, scanPackages);
        for (Class<?> clazz : classes) {
            // 接口名首字母小写
            String mapperName = StringUtil.firstLetterLower(clazz.getSimpleName());
            Object mapperInstance = null;
            if (clazz.isInterface()) {
                // 代理实现
                DaoProxyHandler daoProxyHandler = new DaoProxyHandler();
                mapperInstance = ProxyFactory.getImpl(clazz, daoProxyHandler);
            } else {
                // 如果不是接口，直接使用实例，不代理
                try {
                    mapperInstance = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | IllegalAccessException
                        | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (StringUtil.hasText(mapperName) && mapperInstance != null) {
                this.daos.put(mapperName, mapperInstance);
            }
        }
    }

    /**
     * 根据类型获取DAO实例
     *
     * @param type 类型
     * @return {@link T}
     */
    public <T> T getDao(Class<T> type) {
        for (Map.Entry<String, Object> entry : daos.entrySet()) {
            Object mapper = entry.getValue();
            if (type.isInstance(mapper)) {
                return (T) mapper;
            }
        }
        return null;
    }

    /**
     * 获取所有的dao 实例
     * @return map key为name, value为dao对象
     */
    public Map<String, Object> getDaos() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.daos.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
