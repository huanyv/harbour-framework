package top.huanyv.webmvc.utils.convert;

import top.huanyv.webmvc.utils.ClassDesc;

/**
 * @author huanyv
 * @date 2022/11/10 21:44
 */
public class StringArrayToNumberConverter implements TypeConverter {

    private static final StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        return NUMBER_CONVERTER.convert(((String[])source)[0], targetClassDesc);
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        Class<?> targetType = targetClassDesc.getType();
        return source instanceof String[] &&
                (targetType.isPrimitive()
                || Number.class.isAssignableFrom(targetType)
                || String.class.equals(targetType));
    }
}
