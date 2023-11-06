package top.huanyv.bean.utils;

import top.huanyv.bean.utils.StringUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtil {

    /**
     * 字符串转成整数类型数字，设置一个默认值，转换失败返回默认值
     *
     * @param value        数字字符串
     * @param defaultValue 默认值
     * @return 整数数字
     */
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
        }
        return defaultValue;
    }

    public static <T> T parse(Class<T> type, String value) {
        if (!String.class.equals(type) && !StringUtil.hasText(value)) {
            throw new IllegalArgumentException("string convert '" + type.getName() + "' fail, string 'value' must not be empty!");
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
        return (T) result;
    }

}
