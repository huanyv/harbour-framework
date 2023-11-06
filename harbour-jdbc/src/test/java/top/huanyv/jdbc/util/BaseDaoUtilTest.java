package top.huanyv.jdbc.util;

import org.junit.Test;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.entity.User;
import top.huanyv.bean.utils.Assert;

import java.awt.print.Book;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BaseDaoUtilTest {

    @Test
    public void getType() {
        Class<?> cls = proxy.class;
        Class<?> type = getType(cls);
        System.out.println(type);

        System.out.println("getType(proxy2.class) = " + getType(proxy2.class));

        for (Type genericInterface : proxy2.class.getGenericInterfaces()) {
            System.out.println("genericInterface = " + genericInterface.getTypeName());
        }
    }

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

}


class proxy extends DaoImpl implements Serializable {

}

class proxy2 implements Serializable, dao {

}

class DaoImpl implements dao {

}

interface dao extends BaseDao<User> {

}