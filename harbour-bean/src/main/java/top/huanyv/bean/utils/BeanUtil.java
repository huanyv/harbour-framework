package top.huanyv.bean.utils;

import top.huanyv.bean.utils.JavaBean;
import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.DateUtil;
import top.huanyv.bean.utils.NumberUtil;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public final class BeanUtil {
    private BeanUtil() {
    }

    /**
     * <ul>
     *     <li>把一个<code>Map&lt;String,String[]&gt;</code>集合转成对象</li>
     *     <li>可以用于把<code>request</code>请求参数转成对象</li>
     *     <li>例：<code>User user = BeanUtil.fromMap(req.getParameterMap(), User.class);</code></li>
     *     <li><b>说明：</b></li>
     *     <li>暂时支持8种基本数据类型对应的包装类、<code>BigDecimal</code>、<code>java.util.Date</code>、数组、List集合
     * </ul>
     *
     * @param map  map集合
     * @param type 转成的bean类型
     * @param <T>  对象类型
     * @return 转成的对象
     */
    public static <T> T fromMap(Map<String, String[]> map, Class<T> type) {
        try {
            T t = type.getConstructor().newInstance();

            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                // 属性名
                String name = entry.getKey();
                // 属性值
                String[] values = entry.getValue();

                // 如果类中没有这个属性跳出[本次]循环
                // if (!ReflectUtil.hasField(type, name)) {
                //     continue;
                // }
                writeProperty(t, name, values);
            }
            return t;
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void writeProperty(T t, String fieldName, String[] values) {
        Class<?> type = t.getClass();
        try {
            // 获取属性set方法
            Field field = ReflectUtil.getAllDeclaredField(type, fieldName);
            field.setAccessible(true);
            Method method = ReflectUtil.getSetter(type, field);
            method.setAccessible(true);

            Class<?> fieldType = field.getType();
            if (fieldType.isPrimitive() || Number.class.isAssignableFrom(fieldType)) {
                Object val = NumberUtil.parse(fieldType, values[0]);
                method.invoke(t, val);
            } else if (fieldType.equals(String.class)) {
                method.invoke(t, values[0]);
            } else if (fieldType.equals(Date.class)) {
                method.invoke(t, DateUtil.parse(values[0], DateUtil.DATE_FORMAT));
            } else if (fieldType.equals(List.class)) {
                // 获取List具体类型（泛型）
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    Type typeArgument = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    if (!"?".equals(typeArgument.getTypeName())) {
                        List<Object> list = new ArrayList<>();
                        for (String s : values) {
                            Object o = NumberUtil.parse((Class) typeArgument, s);
                            list.add(o);
                        }
                        method.invoke(t, list);
                    }
                }

            } else if (fieldType.isArray()) {
                // 获取数组具体类型
                Class<?> arrayType = fieldType.getComponentType();
                if (arrayType.isPrimitive() || Number.class.isAssignableFrom(arrayType) || String.class.equals(arrayType)) {
                    // 创建数组实例
                    Object array = Array.newInstance(arrayType, values.length);
                    for (int i = 0; i < values.length; i++) {
                        Object val = NumberUtil.parse(arrayType, values[i]);
                        Array.set(array, i, val);
                    }
                    method.invoke(t, array);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException ignored) {
        }
    }

    public static void copyField(Object source, Object target) {
        Assert.notNull(source, "'source' must not be null.");
        Assert.notNull(target, "'target' must not be null.");
        for (Field field : ReflectUtil.getAllDeclaredFields(source.getClass())) {
            try {
                // 根据源属性找目标同名属性
                String sourceFieldName = field.getName();
                Field targetField = ReflectUtil.getAllDeclaredField(target.getClass(), sourceFieldName);
                // 目标属性setter
                Method targetFieldSetter = ReflectUtil.getSetter(target.getClass(), targetField);
                // 源属性getter
                Method sourceFieldGetter = ReflectUtil.getGetter(source.getClass(), field);
                Object val = sourceFieldGetter.invoke(source);
                targetFieldSetter.invoke(target, val);
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            }
        }
    }

    /**
     * bean的拷贝, 把一个bean的内容拷贝到另一个bean的同名属性中
     */
    public static <T> T copyBean(Object source, Class<T> type) {
        T t = null;
        try {
            t = type.getConstructor().newInstance();
            copyField(source, t);
        } catch (NoSuchMethodException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 拷贝一个list,list中的每一个元素
     */
    public static <T, E> List<T> copyBeanList(List<E> source, Class<T> type) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copyBean(e, type)).collect(Collectors.toList());
    }

    /**
     * 是否是一个java bean <br />
     * 1. 是否有public的属性 <br />
     * 2. 是否有非get(is)和set开头的方法（除去Object的方法） <br />
     * 3. get(is)和set方法的参数数量是否正确，get(is)方法没有入参，set方法只有一个入参 <br />
     * 4. isXxx的方法是否为获取boolean值的方法 <br />
     * 5. 当特殊情况下，可以使用{@link JavaBean @JavaBean}注解
     *
     * @param bean Class<?>
     * @return boolean
     */
    public static boolean isJavaBean(Class<?> bean) {
        if (bean == null) {
            return false;
        }
        JavaBean javaBean = bean.getAnnotation(JavaBean.class);
        if (javaBean != null) {
            return javaBean.value();
        }
        for (Field field : bean.getDeclaredFields()) {
            if (ReflectUtil.isPublic(field)) {
                return false;
            }
        }
        Method[] methods = bean.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (ReflectUtil.isObjectMethod(method) || "canEqual".equals(methodName) || "$jacocoInit".equals(methodName)) {
                continue;
            }
            if (!ReflectUtil.isGetter(method) && !ReflectUtil.isSetter(method)) {
                return false;
            }
        }
        return true;
    }


}
