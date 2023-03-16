package top.huanyv.jdbc.util;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.jdbc.annotation.TableId;
import top.huanyv.jdbc.annotation.TableName;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.tools.utils.Assert;
import top.huanyv.tools.utils.ReflectUtil;
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
     * 获取 BaseDao 的泛型类型 Class
     *
     * @param type 接口或类
     * @return 泛型 Class
     */
    public static Class<?> getType(Type type) {
        Assert.notNull(type, "'type' must not be null.");
        if (type instanceof Class) {
            Class<?> cls = (Class<?>) type;
            if (!cls.isInterface()) {
                Class<?> superclass = cls.getSuperclass();
                if (BaseDao.class.isAssignableFrom(superclass)) {
                    return getType(superclass);
                }
            }
            for (Type genericInterface : cls.getGenericInterfaces()) {
                if (genericInterface instanceof Class) {
                    if (BaseDao.class.isAssignableFrom((Class<?>) genericInterface)) {
                        return getType(genericInterface);
                    }
                }
                if (genericInterface instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) genericInterface).getRawType();
                    if (rawType instanceof Class && BaseDao.class.isAssignableFrom((Class<?>) rawType)) {
                        return getType(genericInterface);
                    }
                }
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (BaseDao.class.equals(parameterizedType.getRawType())) {
                return (Class<?>) parameterizedType.getActualTypeArguments()[0];
            } else {
                return getType(parameterizedType);
            }
        }
        return null;
    }

    /**
     * 获取表id字段名称
     *
     * @param cls Class对象
     * @return table id 的字段名
     */
    public static String getIdColumnName(Class<?> cls) {
        Assert.notNull(cls);
        Field field = getIdField(cls);
        String columnName = JdbcConfigurer.create().isMapUnderscoreToCamelCase()
                ? StringUtil.camelCaseToUnderscore(field.getName()) : field.getName();
        Column column = field.getAnnotation(Column.class);
        return column != null ? column.value() : columnName;
    }

    /**
     * 获取表名
     *
     * @param clazz 类型class
     * @return 表名称
     */
    public static String getTableName(Class<?> clazz) {
        Assert.notNull(clazz);
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return StringUtil.firstLetterLower(clazz.getSimpleName());
    }

    public static Field getIdField(Class<?> cls) {
        Assert.notNull(cls);
        for (Field field : ReflectUtil.getAllDeclaredFields(cls)) {
            if (field.isAnnotationPresent(TableId.class)) {
                return field;
            }
        }
        try {
            return ReflectUtil.getAllDeclaredField(cls, "id");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No id found, please use the '@TableId' annotation on the corresponding id field.");
        }
    }
}
