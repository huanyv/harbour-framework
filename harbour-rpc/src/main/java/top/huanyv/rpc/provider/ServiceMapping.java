package top.huanyv.rpc.provider;

import top.huanyv.tools.utils.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/1/22 16:46
 */
public class ServiceMapping {

    private final Map<String, Class<?>> mapping;

    private final Map<Class<?>, Object> objects;

    public ServiceMapping() {
        this.mapping = new HashMap<>();
        this.objects = new HashMap<>();
    }

    public void add(String serviceName, Class<?> value) {
        this.mapping.put(serviceName, value);
    }

    public Class<?> get(String serviceName) {
        return this.mapping.get(serviceName);
    }

    public boolean contains(String serviceName) {
        return this.mapping.containsKey(serviceName);
    }

    /**
     * 获得实例，为单例对象
     *
     * @param cls cls
     * @return {@link Object}
     */
    public Object getInstance(Class<?> cls) {
        Object result = objects.get(cls);
        if (result == null) {
            result = ReflectUtil.newInstance(cls);
            this.objects.put(cls, result);
        }
        return result;
    }
    public Object getInstance(String serviceName) {
        return getInstance(get(serviceName));
    }

    /**
     * 每次都是新对象
     *
     * @param cls cls
     * @return {@link Object}
     */
    public Object newInstance(Class<?> cls) {
        return ReflectUtil.newInstance(cls);
    }
    public Object newInstance(String serviceName) {
        return newInstance(get(serviceName));
    }
}
