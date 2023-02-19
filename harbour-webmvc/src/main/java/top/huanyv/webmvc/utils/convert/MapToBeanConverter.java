package top.huanyv.webmvc.utils.convert;

import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.webmvc.utils.ClassDesc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author huanyv
 * @date 2022/11/10 21:42
 */
public class MapToBeanConverter implements TypeConverter {

    @Override
    public Object convert(Object source, ClassDesc targetClassDesc) {
        Class<?> targetType = targetClassDesc.getType();
        Object target = ReflectUtil.newInstance(targetType);
        for (Map.Entry<String, String[]> entry : ((Map<String, String[]>)source).entrySet()) {
            // 属性名
            String name = entry.getKey();
            // 属性值
            String[] values = entry.getValue();

            try {
                Field field = ReflectUtil.getAllDeclaredField(targetType, name);
                field.setAccessible(true);
                Method setter = ReflectUtil.getSetter(targetType, field);

                ClassDesc classDesc = ClassDesc.parseField(field);

                Object val = new TypeConverterComposite().convert(values, classDesc);
                ReflectUtil.invokeMethod(setter, target, val);
            } catch (NoSuchMethodException e) {
                // no setter
            } catch (NoSuchFieldException ignored) {
                // no field
            }
        }
        return target;
    }

    @Override
    public boolean isType(Object source, ClassDesc targetClassDesc) {
        return source instanceof Map && BeanUtil.isJavaBean(targetClassDesc.getType());
    }
}
