package top.huanyv.webmvc.utils.convert;

import top.huanyv.tools.utils.StringUtil;
import top.huanyv.webmvc.utils.ClassDesc;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author huanyv
 * @date 2022/11/10 21:44
 */
public class StringToNumberConverter implements TypeConverter {

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        return str2Number(targetClassDesc.getType(), (String) source);
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        Class<?> targetType = targetClassDesc.getType();
        return source instanceof String &&
                (targetType.isPrimitive()
                        || Number.class.isAssignableFrom(targetType)
                        || String.class.equals(targetType));
    }

    public <E> E str2Number(Class<E> type, String value) {
        if (!StringUtil.hasText(value) && !String.class.equals(type)) {
            throw new IllegalArgumentException("string convert " + type.getName() + " fail, string argument is empty!");
        }
        Object result = null;
        switch (type.getName()) {
            case "byte":
            case "java.lang.Byte":
                result = Byte.valueOf(value);
                break;
            case "int":
            case "java.lang.Integer":
                result = Integer.valueOf(value);
                break;
            case "short":
            case "java.lang.Short":
                result = Short.valueOf(value);
                break;
            case "long":
            case "java.lang.Long":
                result = Long.valueOf(value);
                break;
            case "float":
            case "java.lang.Float":
                result = Float.valueOf(value);
                break;
            case "double":
            case "java.lang.Double":
                result = Double.valueOf(value);
                break;
            case "boolean":
            case "java.lang.Boolean":
                result = Boolean.valueOf(value);
                break;
            case "char":
            case "java.lang.Character":
                result = value.charAt(0);
                break;
            case "java.lang.String":
                result = value;
                break;
            case "java.math.BigDecimal":
                result = new BigDecimal(value);
                break;
            case "java.math.BigInteger":
                result = new BigInteger(value);
                break;
            default:
                break;
        }
        return (E) result;
    }
}
