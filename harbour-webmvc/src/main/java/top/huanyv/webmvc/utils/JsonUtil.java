package top.huanyv.webmvc.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import top.huanyv.bean.utils.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * JSON处理工具类，依赖于<code>jackson</code>
 */
public class JsonUtil {

    private static final String FIELD_FILTER_ID = "fieldFilter";

    // 单例
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtil() { }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * 把<code>JSON</code>字符串，转成指定的对象
     *
     * @param json json字符串
     * @param type 指定的类型
     * @param <T>  类型
     * @return 类型
     */
    public static <T> T fromJson(String json, Class<T> type) {
        if (String.class.equals(type)) {
            return (T) json;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据泛型类型把json串转成对象
     *
     * @param json json
     * @param type 类型
     * @return {@link Object}
     */
    public static Object fromJson(String json, ParameterizedType type) {
        return fromJson(json, new TypeReferenceImpl(type));
    }

    /**
     * 把对象转成<code>JSON</code>字符串
     *
     * @param o 对象
     * @return json字符串
     */
    public static String toJson(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof CharSequence) {
            return o.toString();
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonByProperty(Object o, String... property) {
        if (o == null) {
            return null;
        }
        if (o instanceof CharSequence) {
            return o.toString();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(FIELD_FILTER_ID, SimpleBeanPropertyFilter.filterOutAllExcept(property));
        objectMapper.setFilterProvider(filterProvider);
        objectMapper.addMixIn(o.getClass(), FieldFilterMixIn.class);
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonExceptProperty(Object o, String... property) {
        if (o == null) {
            return null;
        }
        if (o instanceof CharSequence) {
            return o.toString();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(FIELD_FILTER_ID, SimpleBeanPropertyFilter.serializeAllExcept(property));
        objectMapper.setFilterProvider(filterProvider);
        objectMapper.addMixIn(o.getClass(), FieldFilterMixIn.class);
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isJson(String json) {
        if (!StringUtil.hasText(json)) {
            return false;
        }
        try {
            OBJECT_MAPPER.readValue(json, Object.class);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @JsonFilter(FIELD_FILTER_ID)
    private interface FieldFilterMixIn { }

    /**
     * TypeReference实现类，更好的实现泛型反射类型转换
     *
     * @author huanyv
     * @date 2022/11/27
     */
    private static class TypeReferenceImpl extends TypeReference<Object> {
        private Type type;
        public TypeReferenceImpl(Type type) {
            this.type = type;
        }
        @Override
        public Type getType() {
            return this.type;
        }
    }

}
