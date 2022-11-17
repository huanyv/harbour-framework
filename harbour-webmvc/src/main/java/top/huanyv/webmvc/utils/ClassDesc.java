package top.huanyv.webmvc.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 类desc
 *
 * @author huanyv
 * @date 2022/11/12 15:42
 */
public class ClassDesc implements AnnotatedElement {

    /**
     * 类描述的原始class
     */
    private Class<?> type;

    /**
     * 注解列表
     */
    private Annotation[] annotations;

    /**
     * 参数化类型，带有<>的类（泛型类型）<br />
     * example: <code>java.util.List&lt;String&gt;</code>
     */
    private ParameterizedType genericType;

    /**
     * 泛型的具体类型
     */
    private Type[] actualTypes;

    /**
     * 如果是数组类型，数组的具体类型
     */
    private Class<?> componentType;

    public ClassDesc() {
    }

    public ClassDesc(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return this.type;
    }

    public boolean isArray() {
        return this.type.isArray();
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return this.annotations;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public ParameterizedType getGenericType() {
        return genericType;
    }

    public void setGenericType(ParameterizedType genericType) {
        this.genericType = genericType;
    }

    public Type[] getActualTypes() {
        return actualTypes;
    }

    public void setActualTypes(Type[] actualTypes) {
        this.actualTypes = actualTypes;
    }

    public Class<?> getComponentType() {
        return componentType;
    }

    public void setComponentType(Class<?> componentType) {
        this.componentType = componentType;
    }

    public static List<ClassDesc> parseClassField(Class<?> cls) {
        List<ClassDesc> classDescList = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            classDescList.add(parseField(field));
        }
        return classDescList;
    }

    public static List<ClassDesc> parseMethodParameter(Method method) {
        List<ClassDesc> classDescList = new ArrayList<>();
        // 方法的注解列表，二维数组
        Annotation[][] methodParameterAnnotations = method.getParameterAnnotations();
        // 类型列表
        Type[] genericTypes = method.getGenericParameterTypes();

        int index = 0;
        for (Type genericType : genericTypes) {
            ClassDesc classDesc = new ClassDesc();
            if (genericType instanceof Class) {
                Class<?> type = (Class<?>) genericType;
                classDesc.setType(type);
                classDesc.setAnnotations(methodParameterAnnotations[index]);
                if (type.isArray()) {
                    classDesc.setComponentType(type.getComponentType());
                }
            } else if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                classDesc.setType((Class<?>) parameterizedType.getRawType());
                classDesc.setAnnotations(methodParameterAnnotations[index]);
                classDesc.setGenericType(parameterizedType);
                classDesc.setActualTypes(parameterizedType.getActualTypeArguments());
            }
            index++;
            classDescList.add(classDesc);
        }
        return classDescList;
    }

    public static ClassDesc parseMethodReturn(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        ClassDesc classDesc = new ClassDesc();
        if (genericReturnType instanceof Class) {
            Class<?> type = (Class<?>) genericReturnType;
            classDesc.setType(type);
            classDesc.setAnnotations(method.getAnnotations());
            if (type.isArray()) {
                classDesc.setComponentType(type.getComponentType());
            }
        } else if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            classDesc.setType((Class<?>) parameterizedType.getRawType());
            classDesc.setAnnotations(method.getAnnotations());
            classDesc.setGenericType(parameterizedType);
            classDesc.setActualTypes(parameterizedType.getActualTypeArguments());
        }
        return classDesc;
    }

    public static ClassDesc parseField(Field field) {
        ClassDesc classDesc = new ClassDesc();
        Type genericType = field.getGenericType();
        // 普通类型
        if (genericType instanceof Class) {
            Class<?> type = (Class<?>) genericType;
            classDesc.setType(type);
            classDesc.setAnnotations(field.getAnnotations());
            // 如果是数组类型
            if (type.isArray()) {
                classDesc.setComponentType(type.getComponentType());
            }
        } else if (genericType instanceof ParameterizedType) {
            // 泛型类型
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            classDesc.setType(field.getType());
            classDesc.setAnnotations(field.getAnnotations());
            classDesc.setGenericType(parameterizedType);
            classDesc.setActualTypes(parameterizedType.getActualTypeArguments());
        }
        return classDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDesc classDesc = (ClassDesc) o;
        return Objects.equals(type, classDesc.type)
                && Arrays.equals(annotations, classDesc.annotations)
                && Objects.equals(genericType, classDesc.genericType)
                && Arrays.equals(actualTypes, classDesc.actualTypes)
                && Objects.equals(componentType, classDesc.componentType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, genericType, componentType);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + Arrays.hashCode(actualTypes);
        return result;
    }

    @Override
    public String toString() {
        return "ClassDesc{" +
                "type=" + type +
                ", annotations=" + Arrays.toString(annotations) +
                ", genericType=" + genericType +
                ", actualTypes=" + Arrays.toString(actualTypes) +
                ", componentType=" + componentType +
                '}';
    }
}
