package top.huanyv.utils;

import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;

public class StringUtil {
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };

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

    public static String removePrefix(String str, String prefix) {
        if (str.startsWith(prefix)) {
            str = str.substring(prefix.length());
        }
        return str;
    }

    public static String addPrefix(String str, String prefix) {
        if (!str.startsWith(prefix)) {
            str = prefix + str;
        }
        return str;
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
}
