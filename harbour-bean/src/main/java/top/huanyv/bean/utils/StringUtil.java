package top.huanyv.bean.utils;

import top.huanyv.bean.utils.NumberUtil;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class StringUtil {

    public static final String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};


    /**
     * 把一个字符串重复count次
     */
    public static String repeatStr(String str, int count) {
        return repeatStr(str, count, "");
    }

    /**
     * 把一个字符串重复count次，指定间隔符号
     */
    public static String repeatStr(String str, int count, String separator) {
        StringJoiner joiner = new StringJoiner(separator);
        for (int i = 0; i < count; i++) {
            joiner.add(str);
        }
        return joiner.toString();
    }

    /**
     * 单词首字母大写
     *
     * @param s 单词
     * @return 首字母大写
     */
    public static String firstLetterUpper(String s) {
        if (s == null) {
            return s;
        }
        char[] chars = s.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
        }
        return new String(chars);
    }

    /**
     * 单词首字母小写
     *
     * @param s 单词
     * @return 首字母小写
     */
    public static String firstLetterLower(String s) {
        if (s == null) {
            return s;
        }
        char[] chars = s.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return new String(chars);
    }

    /**
     * 驼峰转下划线, userName --> user_name
     *
     * @param name 名字
     * @return {@link String}
     */
    public static String camelCaseToUnderscore(String name) {
        if (!hasText(name)) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 'A' && chars[i] <= 'Z') && i != 0) {
                char val = (char) (chars[i] + 32);
                sb.append("_").append(val);
                continue;
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰,   user_name  ->   userName
     *
     * @param name 名字
     * @return {@link String}
     */
    public static String underscoreToCamelCase(String name) {
        if (!hasText(name)) {
            return name;
        }
        String[] strings = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                sb.append(StringUtil.firstLetterUpper(strings[i]));
                continue;
            }
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    /**
     * 删除一个字符串中所有的空格
     *
     * @param s 字符串
     * @return {@link String}
     */
    public static String removeSpace(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 删除字符串指定前缀
     *
     * @param s      字符串
     * @param prefix 前缀
     * @return string
     */
    public static String removePrefix(String s, String prefix) {
        if (s == null) {
            return s;
        }
        if (!s.startsWith(prefix)) {
            return s;
        }
        return s.substring(prefix.length());
    }

    /**
     * 删除后缀
     *
     * @param s      字符串
     * @param suffix 后缀
     * @return {@link String}
     */
    public static String removeSuffix(String s, String suffix) {
        if (s == null) {
            return s;
        }
        if (!s.endsWith(suffix)) {
            return s;
        }
        return s.substring(0, s.length() - suffix.length());
    }

    /**
     * 添加前缀
     *
     * @param s      字符串
     * @param prefix 前缀
     * @return {@link String}
     */
    public static String addPrefix(String s, String prefix) {
        if (s.startsWith(prefix)) {
            return s;
        }
        return prefix + s;
    }

    /**
     * 添加后缀
     *
     * @param s      字符串
     * @param suffix 后缀
     * @return {@link String}
     */
    public static String addSuffix(String s, String suffix) {
        if (s.endsWith(suffix)) {
            return s;
        }
        return s + suffix;
    }

    /**
     * 格式化占位符字符串
     *
     * @param str  字符串
     * @param args 占位符参数
     * @return string
     */
    public static String format(String str, Object... args) {
        for (Object arg : args) {
            str = str.replaceFirst("\\{}", arg.toString());
        }
        return str;
    }

    /**
     * 字符串分割成指定类型的Set集合
     *
     * @param str 字符串
     * @param separator 分割符
     * @param type 类型
     * @return {@link Set}<{@link T}>
     */
    public static <T> Set<T> parseToSet(String str, String separator, Class<T> type) {
        if (isEmpty(str)) {
            return Collections.emptySet();
        }
        Set<T> result = new HashSet<>();
        String[] strings = str.split(separator);
        for (String s : strings) {
            T val = null;
            try {
                val = NumberUtil.parse(type, s);
            } catch (Exception e) {
                // e.printStackTrace();
            }
            // if (val == null) {
            //     throw new RuntimeException("'" + s + "' cannot be resolved to type '" + type.getName() + "'");
            // }
            if (val != null) {
                result.add(val);
            }
        }
        return result;
    }

    /**
     * 忽略大小写比较，可以避免空指针异常
     *
     * @param s1 s1
     * @param s2 s2
     * @return boolean
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        return s1 != null && s1.equalsIgnoreCase(s2);
    }

    /**
     * 对象属性打印成字符串，调用toString方法
     *
     * @param o 对象
     * @return {@link String}
     */
    public static String toString(Object o) {
        Class<?> cls = o.getClass();
        try {
            // 判断是否重写了 toString 方法
            Method method = cls.getDeclaredMethod("toString");
            return o.toString();
        } catch (NoSuchMethodException ignored) {
        }
        try {
            String name = cls.getSimpleName();
            StringBuilder sb = new StringBuilder(name).append("{");
            Field[] fields = ReflectUtil.getAllDeclaredFields(cls);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldVal = field.get(o);
                sb.append(fieldName).append("=");
                // 处理Array类型
                if (field.getType().isArray()) {
                    sb.append(Arrays.toString((Object[]) fieldVal));
                } else {
                    sb.append(fieldVal.toString());
                }
                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.append("}").toString();
        } catch (IllegalAccessException ignored) {
        }
        return o.toString();
    }

    /**
     * 获取UUID，没有间隔符
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取短UUID
     */
    public static String getShortUUID() {
        StringBuilder sb = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            sb.append(chars[x % 0x3E]);
        }
        return sb.toString();
    }

    /**
     * 得到一个指定长度的随机字符串
     */
    public static String randomStr(int len) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(chars.length - 26);
            result.append(chars[index]);
        }
        return result.toString();
    }

    /**
     * 判断是否有文本（字符串非NULL值，并且不能是全空格）
     */
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否是空串
     */
    public static boolean hasLength(String str) {
        return !isNull(str) && str.length() != 0;
    }

    /**
     * 判断字符串是不是NULL
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
