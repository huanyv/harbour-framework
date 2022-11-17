package top.huanyv.webmvc.core.request.type;

import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Array;

/**
 * @author huanyv
 * @date 2022/11/11 15:47
 */
public class StringArrayConverter implements TypeConverter<String[], Object> {

    private final static StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    @Override
    public Object convert(String[] source, ClassDesc targetClassDesc) {
        Class<?> arrayType = targetClassDesc.getComponentType();
        if(String.class.equals(arrayType)) {
            return source;
        }
        Object targetArray = Array.newInstance(arrayType, source.length);
        for (int i = 0; i < source.length; i++) {
            Object val = NUMBER_CONVERTER.convert(source[i], new ClassDesc(arrayType));
            Array.set(targetArray, i, val);
        }
        return targetArray;
    }

    @Override
    public boolean isType(Class<?> sourceType, Class<?> targetType) {
        return sourceType != null && sourceType.isArray() && targetType != null && targetType.isArray();
    }
}
