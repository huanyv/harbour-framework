package top.huanyv.jdbc.util;

import top.huanyv.jdbc.annotation.Column;
import top.huanyv.tools.utils.BeanUtil;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huanyv
 * @date 2022/9/9 10:10
 */
public class SqlParamParser {

    /**
     * 占位符正则表达式
     */
    public static final String PLACEHOLDER_REGEX = "#\\{[0-9a-zA-Z]+}";

    private static final Pattern PATTERN = Pattern.compile(PLACEHOLDER_REGEX);

    public static SqlAndArgs parse(String placeholderSql, Object... params) {

        // 如果没有使用占位符
        if (!PATTERN.matcher(placeholderSql).find()) {
            return new SqlAndArgs(placeholderSql, params);
        }

        // sql参数映射
        Map<String, Object> sqlParamMapping = new HashMap<>();
        // 非自定义变量类型index,  arg1, arg2, arg3, .....
        int index = 0;
        // 填充参数映射
        for (Object param : params) {
            Class<?> cls = param.getClass();
            // 是否常用类型: 包装类， 基本数据类型， 字符类型
            if (ClassUtil.isCommonType(cls)) {
                sqlParamMapping.put("arg" + index, param);
            }
            index++;
            // 自定义映射
            if (Map.class.isAssignableFrom(cls)) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) param).entrySet()) {
                    sqlParamMapping.put(entry.getKey().toString(), entry.getValue());
                }
            }
            // 是否JavaBean
            if (BeanUtil.isJavaBean(cls)) {
                // 获取属性名与值
                for (Field field : cls.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object val = null;
                    try {
                        val = field.get(param);
                    } catch (IllegalAccessException e) {
                        val = null;
                    }
                    sqlParamMapping.put(fieldName, val);
                }
            }
        }

        String sql = placeholderSql.replaceAll(PLACEHOLDER_REGEX, "?");
        List<Object> args = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(placeholderSql);
        while (matcher.find()) {
            String group = matcher.group();
            String paramName = group.substring(2, group.length() - 1);
            Object val = sqlParamMapping.get(paramName);
            args.add(val);
        }

        return new SqlAndArgs(sql, args.toArray());
    }

}
