package top.huanyv.webmvc.core.request.type;

import top.huanyv.webmvc.utils.ClassDesc;

/**
 * 类型转换器
 *
 * @author huanyv
 * @date 2022/11/10 16:29
 */
public interface TypeConverter<S, T> {

    T convert(S source, ClassDesc targetClassDesc);

    boolean isType(Class<?> sourceType, Class<?> targetType);

}
