package top.huanyv.webmvc.utils.convert;

import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/11 15:47
 */
public class StringListConverter implements TypeConverter {

    private final static StringToNumberConverter NUMBER_CONVERTER = new StringToNumberConverter();

    public Object convert(Object source, ClassDesc targetClassDesc) {
        Type targetType = targetClassDesc.getActualTypes()[0];
        String[] strings = (String[]) source;
        if (!(targetType instanceof Class) && "?".equals(targetType.getTypeName())) {
            return null;
        }
        List<Object> targetList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            Object val = NUMBER_CONVERTER.convert(strings[i], new ClassDesc((Class<?>) targetType));
            targetList.add(val);
        }
        return targetList;
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        return source != null && source.getClass().isArray() && List.class.equals(targetClassDesc.getType());
    }
}
