package top.huanyv.webmvc.core.request.type;

import top.huanyv.webmvc.utils.ClassDesc;

/**
 * @author huanyv
 * @date 2022/11/10 21:44
 */
public class StringArrayToNumberConverter implements TypeConverter<String[], Object> {

    private static final StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    @Override
    public Object convert(String[] source, ClassDesc targetClassDesc) {
        return NUMBER_CONVERTER.convert(source[0], targetClassDesc);
    }

    @Override
    public boolean isType(Class<?> sourceType, Class<?> targetType) {
        return String[].class.equals(sourceType)
                && (targetType.isPrimitive()
                || Number.class.isAssignableFrom(targetType)
                || String.class.equals(targetType));
    }
}
