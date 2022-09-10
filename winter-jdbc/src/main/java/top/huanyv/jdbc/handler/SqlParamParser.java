package top.huanyv.jdbc.handler;

import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.NumberUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static final String PLACEHOLDER_REGEX = "\\#\\{[0-9a-zA-Z]+\\}";

    /**
     * sql参数映射
     */
    private Map<String, Object> sqlParamMapping  = new HashMap<>();

    private String sql;

    private List<Object> params;

    public SqlParamParser(String placeholderSql, Object... args) {
        this.params = new ArrayList<>();

        int index = 0;
        // 填充参数映射
        for (Object arg : args) {
            Class<?> clazz = arg.getClass();
            if (NumberUtil.isCommonType(clazz)) {
                this.sqlParamMapping.put("arg" + index, arg);
                index++;
            }
            if (ClassUtil.isCustomClass(clazz)) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object o = null;
                    try {
                        o = field.get(arg);
                    } catch (IllegalAccessException e) {
                    }
                    this.sqlParamMapping.put(name, o);
                }
            }
        }

        Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
        Matcher matcher = pattern.matcher(placeholderSql);
        while (matcher.find()) {
            String group = matcher.group();
            String paramName = group.substring(2, group.length() - 1);
            Object val = sqlParamMapping.get(paramName);
            this.params.add(val);
        }

        this.sql = placeholderSql.replaceAll(PLACEHOLDER_REGEX, "?");
    }

    public String getSql() {
        return this.sql;
    }

    public Object[] getArgs() {
        return this.params.toArray();
    }


}
