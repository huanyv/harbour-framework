package top.huanyv.bean.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 拆箱和装箱工具类
 *
 * @author huanyv
 * @date 2023/11/27 14:57
 */
public class BasicType {

    public static final Map<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<>();

    public static final Map<Class<?>, Class<?>> WRAPPER_MAP = new HashMap<>();

    static {
        PRIMITIVE_MAP.put(byte.class, Byte.class);
        PRIMITIVE_MAP.put(short.class, Short.class);
        PRIMITIVE_MAP.put(int.class, Integer.class);
        PRIMITIVE_MAP.put(long.class, Long.class);
        PRIMITIVE_MAP.put(float.class, Float.class);
        PRIMITIVE_MAP.put(double.class, Double.class);
        PRIMITIVE_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_MAP.put(char.class, Character.class);

        WRAPPER_MAP.put(Byte.class, byte.class);
        WRAPPER_MAP.put(Short.class, short.class);
        WRAPPER_MAP.put(Integer.class, int.class);
        WRAPPER_MAP.put(Long.class, long.class);
        WRAPPER_MAP.put(Float.class, float.class);
        WRAPPER_MAP.put(Double.class, double.class);
        WRAPPER_MAP.put(Boolean.class, boolean.class);
        WRAPPER_MAP.put(Character.class, char.class);
    }

    /**
     * 根据基本数据类型，获取其包装类型
     *
     * @param type 基本数据类型
     * @return 包装类
     */
    public static Class<?> getWrapper(Class<?> type) {
        if (type == null) {
            return null;
        }
        if (!type.isPrimitive()) {
            return type;
        }
        Class<?> result = PRIMITIVE_MAP.get(type);
        return result == null ? type : result;
    }

    /**
     * 根据包装类型，获取其基本数据类型
     *
     * @param type 包装类型
     * @return 基本数据类型
     */
    public static Class<?> getPrimitive(Class<?> type) {
        if (type == null) {
            return null;
        }
        if (type.isPrimitive()) {
            return type;
        }
        Class<?> result = WRAPPER_MAP.get(type);
        return result == null ? type : result;
    }
}
