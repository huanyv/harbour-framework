package top.huanyv.bean.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * @author huanyv
 * @date 2023/11/27 16:42
 */
public class ArrayUtil {

    public static <S, T> T[] map(S[] source, Class<T> target, Function<S, ? extends T> fun) {
        T[] result = newArray(target, source.length);
        for (int i = 0; i < source.length; i++) {
            result[i] = fun.apply(source[i]);
        }
        return result;
    }

    public static <T> List<T> toList(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }

    public static <T> Set<T> toSet(T... t) {
        return new HashSet<>(Arrays.asList(t));
    }

    /**
     * 创建数组
     *
     * @param type 类型
     * @param len  数组长度
     * @return {@link T[]} 泛型
     */
    public static <T> T[] newArray(Class<T> type, int len) {
        return (T[]) Array.newInstance(type, len);
    }

}
