package top.huanyv.jdbc.core.datasource;

/**
 * @author huanyv
 * @date 2023/1/9 14:41
 */
public final class DataSourceKeyHolder {

    private static final ThreadLocal<String> DATASOURCE_KEY = new ThreadLocal<>();

    public static String get() {
        return DATASOURCE_KEY.get();
    }

    public static void set(String key) {
        DATASOURCE_KEY.set(key);
    }

    public static void remove() {
        DATASOURCE_KEY.remove();
    }

}
