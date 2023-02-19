package top.huanyv.webmvc.utils.convert;

import top.huanyv.webmvc.utils.ClassDesc;

import java.util.Date;

/**
 * @author huanyv
 * @date 2022/11/10 21:44
 */
public class StringArrayToDateConverter implements TypeConverter {

    private static final StringToDateConverter DATE_CONVERTER = new StringToDateConverter();

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        return DATE_CONVERTER.convert(((String[])source)[0], targetClassDesc);
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        Class<?> targetType = targetClassDesc.getType();
        return source instanceof String[] && Date.class.equals(targetType);
    }
}
