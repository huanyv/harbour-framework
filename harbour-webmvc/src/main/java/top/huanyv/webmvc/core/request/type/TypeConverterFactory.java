package top.huanyv.webmvc.core.request.type;

import java.util.HashSet;
import java.util.Set;

/**
 * @author huanyv
 * @date 2022/11/10 16:33
 */
public class TypeConverterFactory {

    private static Set<TypeConverter> converters = new HashSet<>();

    static {
        converters.add(new StringToNumberConverter());
        converters.add(new StringArrayToNumberConverter());
        converters.add(new StringToDateConverter());
        converters.add(new StringArrayConverter());
        converters.add(new StringListConverter());
        converters.add(new MapToBeanConverter());
    }

    public static TypeConverter getTypeConverter(Class<?> sourceType, Class<?> targetType) {
        for (TypeConverter converter : converters) {
            if (converter.isType(sourceType, targetType)) {
                return converter;
            }
        }
        return null;
    }

}
