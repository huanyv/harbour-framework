package top.huanyv.bean.utils;

import top.huanyv.bean.utils.ClassLoaderUtil;
import top.huanyv.bean.utils.NumberUtil;
import top.huanyv.bean.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

    /**
     * 从类路径下加载<code>properties</code>文件，通过<code>key</code>获取值
     *
     * @param fileName 文件名
     * @param key      键
     * @return 值，没有找到返回NULL
     */
    public static String getProperty(String fileName, String key) {
        return getProperty(fileName, key, null);
    }

    /**
     * 从类路径下加载<code>properties</code>文件，通过<code>key</code>获取值
     *
     * @param fileName     文件名
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getProperty(String fileName, String key, String defaultValue) {
        InputStream stream = ClassLoaderUtil.getInputStream(fileName);
        if (stream != null) {
            Properties properties = new Properties();
            try {
                properties.load(stream);
                return properties.getProperty(key, defaultValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }


    public static Properties getProperties(String fileName) {
        InputStream inputStream = ClassLoaderUtil.getInputStream(fileName);
        Properties properties = new Properties();
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static Properties load(InputStream inputStream) {
        Properties properties = new Properties();
        if (inputStream == null) {
            return properties;
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Map<String, String> loadMap(InputStream inputStream) {
        return toMap(load(inputStream));
    }

    public static Map<String, String> toMap(Properties properties) {
        Map<String, String> map = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

}
