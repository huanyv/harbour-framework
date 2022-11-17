package top.huanyv.webmvc.core.request.type;

import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.tools.utils.FieldUtil;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.*;
import java.util.Map;

/**
 * @author huanyv
 * @date 2022/11/10 21:42
 */
public class MapToBeanConverter implements TypeConverter<Map<String, String[]>, Object> {

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Map<String, String[]> source, ClassDesc targetClassDesc) {
        Class<?> targetType = targetClassDesc.getType();
        Object target = ReflectUtil.newInstance(targetType);
        for (Map.Entry<String, String[]> entry : source.entrySet()) {
            // 属性名
            String name = entry.getKey();
            // 属性值
            String[] values = entry.getValue();

            // 如果类中没有这个属性跳出[本次]循环
            if (!FieldUtil.hasField(targetType, name)) {
                continue;
            }

            try {
                Field field = targetType.getDeclaredField(name);
                field.setAccessible(true);
                Method method = FieldUtil.getSetterMethod(targetType, field);
                method.setAccessible(true);

                ClassDesc classDesc = ClassDesc.parseField(field);

                Object val = null;
                TypeConverter typeConverter = TypeConverterFactory.getTypeConverter(String[].class, classDesc.getType());
                val = typeConverter == null ? null : typeConverter.convert(values, classDesc);
                method.invoke(target, val);
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return target;
    }

    @Override
    public boolean isType(Class<?> sourceType, Class<?> targetType) {
        return Map.class.isAssignableFrom(sourceType) && BeanUtil.isJavaBean(targetType);
    }


}
