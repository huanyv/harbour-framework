package top.huanyv.jdbc.core;

import top.huanyv.jdbc.anno.Dao;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @date 2022/7/22 15:54
 */
public class MapperScanner {

    private JdbcConfigurer config = JdbcConfigurer.create();

    // 所有的mapper代理实例
    private Map<String, Object> mappers = new ConcurrentHashMap<>();

    public MapperScanner() {
        String scanPackages = config.getScanPackages();
        // 扫描包
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(scanPackages, Dao.class);
        for (Class<?> clazz : classes) {
            // 接口名首字母小写
            String mapperName = StringUtil.firstLetterLower(clazz.getSimpleName());
            Object mapperInstance = null;
            if (clazz.isInterface()) {
                // 代理实现
                mapperInstance = ProxyFactory.getImpl(clazz, new MapperProxyHandler());
            } else {
                try {
                    mapperInstance = clazz.getConstructor().newInstance();
                } catch (NoSuchMethodException | IllegalAccessException
                        | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (StringUtil.hasText(mapperName) && mapperInstance != null) {
                this.mappers.put(mapperName, mapperInstance);
            }
        }
    }

}
