package top.huanyv.jdbc.util;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author huanyv
 * @date 2022/9/1 10:21
 */
public class BaseDaoUtil {

    /**
     * 获取 baseDao 的泛型类型 Class
     * @param type 接口或类
     * @return 泛型 Class
     */
    public static Class<?> getType(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            for (Type genericInterface : clazz.getGenericInterfaces()) {
                return getType(genericInterface);
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (BaseDao.class.equals(parameterizedType.getRawType())) {
                Class actualTypeArgument = (Class) parameterizedType.getActualTypeArguments()[0];
                return actualTypeArgument;
            } else {
                return getType(parameterizedType);
            }
        }
        return null;
    }

    /**
     * 获取表id名
     * @param clazz clazz对象
     * @return table id 的字段名
     */
    public static String getTableId(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableId.class)) {
                Column column = field.getAnnotation(Column.class);
                return column != null ? column.value() : field.getName();
            }
        }
        return "id";
    }

    /**
     * 获取表名
     * @param clazz 类型class
     * @return 表名称
     */
    public static String getTableName(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return StringUtil.firstLetterLower(clazz.getSimpleName());
    }
}
