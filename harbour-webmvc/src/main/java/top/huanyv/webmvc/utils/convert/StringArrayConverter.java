package top.huanyv.webmvc.utils.convert;

import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Array;

/**
 * @author huanyv
 * @date 2022/11/11 15:47
 */
public class StringArrayConverter implements TypeConverter {

    private final static StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        Class<?> arrayType = targetClassDesc.getComponentType();
        if(String.class.equals(arrayType)) {
            return source;
        }
        String[] strings = (String[]) source;
        Object targetArray = Array.newInstance(arrayType, strings.length);
        for (int i = 0; i < strings.length; i++) {
            Object val = NUMBER_CONVERTER.convert(strings[i], new ClassDesc(arrayType));
            Array.set(targetArray, i, val);
        }
        return targetArray;
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        return source != null && source.getClass().isArray() && targetClassDesc.isArray();
    }
}
