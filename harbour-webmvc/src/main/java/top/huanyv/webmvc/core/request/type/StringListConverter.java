package top.huanyv.webmvc.core.request.type;

import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/11 15:47
 */
public class StringListConverter implements TypeConverter<String[], List>{

    private final static StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    @Override
    public List convert(String[] source, ClassDesc targetClassDesc) {
        Type targetType = targetClassDesc.getActualTypes()[0];
        if (!(targetType instanceof Class) && "?".equals(targetType.getTypeName())) {
            return null;
        }
        List<Object> targetList = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            Object val = NUMBER_CONVERTER.convert(source[i], new ClassDesc((Class<?>) targetType));
            targetList.add(val);
        }
        return targetList;
    }

    @Override
    public boolean isType(Class<?> sourceType, Class<?> targetType) {
        return sourceType != null && sourceType.isArray() && List.class.equals(targetType);
    }
}
