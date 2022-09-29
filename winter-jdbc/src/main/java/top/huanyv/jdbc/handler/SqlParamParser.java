package top.huanyv.jdbc.handler;

import javafx.beans.binding.ObjectExpression;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.NumberUtil;

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
    public static final String PLACEHOLDER_REGEX = "\\#\\{[0-9a-zA-Z]+\\}";

    public SqlAndArgs parse(String placeholderSql, Object... params) {

        // 如果没有使用占位符
        if (!Pattern.compile(PLACEHOLDER_REGEX).matcher(placeholderSql).find()) {
            return new SqlAndArgs(placeholderSql, params);
        }

        // sql参数映射
        Map<String, Object> sqlParamMapping  = new HashMap<>();
        // 非自定义变量类型index,  arg1, arg2, arg3, .....
        int index = 0;
        // 填充参数映射
        for (Object param : params) {
            Class<?> clazz = param.getClass();
            // 是否常用类型: 包装类， 基本数据类型， string
            if (ClassUtil.isCommonType(clazz)) {
                sqlParamMapping.put("arg" + index, param);
                index++;
            }
            // 自定义映射
            if (Map.class.isAssignableFrom(clazz)) {
                sqlParamMapping.putAll((Map<? extends String, ?>) param);
            }
            // 是否自定义类型
            if (ClassUtil.isCustomClass(clazz)) {
                // 获取属性名与值
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object o = null;
                    try {
                        o = field.get(param);
                    } catch (IllegalAccessException e) {
                        o = null;
                    }
                    sqlParamMapping.put(name, o);
                }
            }
        }
        List<Object> args = new ArrayList<>();
        String sql = "";
        Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
        Matcher matcher = pattern.matcher(placeholderSql);
        while (matcher.find()) {
            String group = matcher.group();
            String paramName = group.substring(2, group.length() - 1);
            Object val = sqlParamMapping.get(paramName);
            args.add(val);
        }

        sql = placeholderSql.replaceAll(PLACEHOLDER_REGEX, "?");

        return new SqlAndArgs(sql, args.toArray());
    }

    public static class SqlAndArgs {
        private String sql;
        private Object[] args;

        public SqlAndArgs() {
        }

        public SqlAndArgs(String sql, Object[] args) {
            this.sql = sql;
            this.args = args;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        @Override
        public String toString() {
            return "SqlAndArgs{" +
                    "sql='" + sql + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
