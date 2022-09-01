package top.huanyv.jdbc.util;

import top.huanyv.jdbc.builder.BaseDao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author huanyv
 * @date 2022/9/1 10:21
 */
public class BaseDaoUtil {
//    public static Class<?> getType(Class<? extends BaseDao> clazz) {
//
//        for (Type genericInterface : clazz.getGenericInterfaces()) {
//            if (genericInterface.equals(BaseDao.class)) {
//                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
//                Class actualTypeArgument = (Class) parameterizedType.getActualTypeArguments()[0];
//                return actualTypeArgument;
//            }
//            Class anInterface = (Class) genericInterface;
//            for (Type type : anInterface.getGenericInterfaces()) {
//                if (type instanceof ParameterizedType) {
//                    ParameterizedType parameterizedType = (ParameterizedType) type;
//                    if (BaseDao.class.equals(parameterizedType.getRawType())) {
//                        Class actualTypeArgument = (Class) parameterizedType.getActualTypeArguments()[0];
//                        return actualTypeArgument;
//                    }
//                }
//            }
//        }
//        return null;
//    }
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
}
