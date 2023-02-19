package top.huanyv.webmvc.utils.convert;


import top.huanyv.webmvc.utils.ClassDesc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huanyv
 * @date 2022/11/10 21:44
 */
public class TypeConverterComposite implements TypeConverter {

    private final List<TypeConverter> converters = new ArrayList<>();

    public TypeConverterComposite() {
        converters.add(new MapToBeanConverter());
        converters.add(new StringArrayConverter());
        converters.add(new StringListConverter());
        converters.add(new StringToNumberConverter());
        converters.add(new StringToDateConverter());
        converters.add(new StringArrayToNumberConverter());
        converters.add(new StringArrayToDateConverter());
    }

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        TypeConverter converter = getConverter(source, targetClassDesc);
        if (converter == null) {
            return null;
        }
        return converter.convert(source, targetClassDesc);
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        return getConverter(source, targetClassDesc) != null;
    }

    private TypeConverter getConverter(Object source, ClassDesc classDesc) {
        for (TypeConverter converter : converters) {
            if (converter.isType(source, classDesc)) {
                return converter;
            }
        }
        return null;
    }

}
