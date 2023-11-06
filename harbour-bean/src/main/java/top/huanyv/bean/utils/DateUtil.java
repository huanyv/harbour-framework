package top.huanyv.bean.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 把字符串日期转成<code>yyyy-MM-dd</code>格式的Date类型
     * @param dateStr 字符串日期
     * @return java.util.Date
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DATE_FORMAT);
    }

    /**
     * 把字符串日期转成指定格式的Date类型
     * @param dateStr 字符串日期
     * @param format 格式
     * @return Date
     */
    public static Date parse(String dateStr, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * 以指定格式格式化一个日期
     */
    public static String format(Date date, String formatter) {
        return new SimpleDateFormat(formatter).format(date);
    }

    /**
     * 得到当前的日期
     */
    public static String nowDate() {
        return format(new Date());
    }

    /**
     * 得到当前日期时间
     */
    public static String nowDateTime() {
        return format(new Date(), DATE_TIME_FORMAT);
    }

    /**
     * 获取当前日期的纯数字格式
     */
    public static String nowDateNumber() {
        return format(new Date(), "yyyyMMdd");
    }
}
