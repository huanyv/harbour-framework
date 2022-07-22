package top.huanyv.jdbc.core;

import top.huanyv.ioc.utils.ClassUtil;
import top.huanyv.jdbc.anno.Mapper;
import top.huanyv.utils.StringUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @date 2022/7/22 15:54
 */
public class MapperScanner {
    private String scanPack;
    private DataSource dataSource;

    // 所有的mapper代理实例
    private Map<String, Object> mappers = new ConcurrentHashMap<>();

    // 动态代理实现接口
    void loadMapper(InvocationHandler invocationHandler) {
        // 扫描包
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(scanPack, Mapper.class);
        for (Class<?> clazz : classes) {
            // 接口名首字母小写
            String mapperName = StringUtil.firstLetterLower(clazz.getSimpleName());
            // 代理实现
            Object o = ProxyFactory.getImpl(clazz, invocationHandler);
            this.mappers.put(mapperName, o);
        }
    }

    public String getScanPack() {
        return scanPack;
    }

    public void setScanPack(String scanPack) {
        this.scanPack = scanPack;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, Object> getMappers() {
        return mappers;
    }

    @Override
    public String toString() {
        return "MapperConfigurer{" +
                "scanPack='" + scanPack + '\'' +
                ", dataSource=" + dataSource +
                '}';
    }
}
