package top.huanyv.bean.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author huanyv
 * @date 2022/7/9 14:37
 */
public class ResourceUtil {

    public static String readStr(String name) {
        return readStr(name, null);
    }

    public static String readStr(String name, String defaultValue) {
        InputStream inputStream = ClassLoaderUtil.getInputStream(name);
        if (inputStream == null) {
            return defaultValue;
        }
        return IoUtil.readStr(inputStream, StandardCharsets.UTF_8);
    }
}
