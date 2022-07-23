package top.huanyv.jdbc.core;

import top.huanyv.jdbc.anno.Mapper;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @date 2022/7/22 15:54
 */
public class MapperScanner {

    // 持久层接口所在的包
    private String scanPack;

    // 所有的mapper代理实例
    private Map<String, Object> mappers = new ConcurrentHashMap<>();

    public MapperScanner() { }

    public MapperScanner(String scanPack) {
        this.scanPack = scanPack;
    }

    // 动态代理实现接口
    void loadMapper(InvocationHandler invocationHandler) {
        // 扫描包
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(scanPack, Mapper.class);
        for (Class<?> clazz : classes) {
            // 接口名首字母小写
            String mapperName = StringUtil.firstLetterLower(clazz.getSimpleName());
            Object mapperInstance = null;
            if (clazz.isInterface()) {
                // 代理实现
                mapperInstance = ProxyFactory.getImpl(clazz, invocationHandler);
            } else if (clazz.isLocalClass()) {
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

    public String getScanPack() {
        return scanPack;
    }

    public void setScanPack(String scanPack) {
        this.scanPack = scanPack;
    }

    public Map<String, Object> getMappers() {
        return mappers;
    }

    @Override
    public String toString() {
        return "MapperConfigurer{" +
                "scanPack='" + scanPack + '\'' +
                '}';
    }
}
